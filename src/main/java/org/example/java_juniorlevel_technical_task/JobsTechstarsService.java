package org.example.java_juniorlevel_technical_task;

import com.opencsv.CSVWriter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class JobsTechstarsService {

    private final JobRepository jobRepository;

    @Autowired
    public JobsTechstarsService(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    public void fetchAndPrintJobsDataWithDescriptionAndFilteredLaborFunctionFromUrl() {
        String url = "https://jobs.techstars.com/jobs?filter=eyJqb2JfZnVuY3Rpb25zIjpbIkFjY291bnRpbmcgJiBGaW5hbmNlIl19";
        try {
            Document document = Jsoup.connect(url).get();
            Elements jobs = document.select("div[itemtype='https://schema.org/JobPosting']");

            for (Element job : jobs) {
                String jobPageTechstarsUrl = job.select("a[data-testid='link']").attr("href");
                String absoluteJobPageTechstarsUrl = getAbsoluteUrl(url, jobPageTechstarsUrl);

                String positionName = job.select(".sc-beqWaB.kToBwF").text();
                String organizationUrl = job.select("a[data-testid='job-title-link']").attr("href");
                organizationUrl = getAbsoluteUrl(url, organizationUrl);
                String logoUrl = job.select("meta[itemprop='logo']").attr("content");
                String organizationTitle = job.select("meta[itemprop='name']").attr("content");
                String laborFunction = getFilteredLaborFunctionFromUrl(absoluteJobPageTechstarsUrl);
                String location = job.select("meta[itemprop='address']").attr("content");
                String postedDate = job.select("meta[itemprop='datePosted']").attr("content");
                String description = getDescriptionFromUrl(absoluteJobPageTechstarsUrl);
                Elements tags = job.select("div[data-testid='tag']");
                StringBuilder tagsText = new StringBuilder();
                for (Element tag : tags) {
                    tagsText.append(tag.select(".sc-dmqHEX.dncTlc").text()).append(", ");
                }
                String tagsString = tagsText.toString().replaceAll(", $", "");

                positionName = checkAndReplaceIfEmpty(positionName, "Position Name");
                organizationUrl = checkAndReplaceIfEmpty(organizationUrl, "Organization URL");
                logoUrl = checkAndReplaceIfEmpty(logoUrl, "Logo URL");
                organizationTitle = checkAndReplaceIfEmpty(organizationTitle, "Organization Title");
                laborFunction = checkAndReplaceIfEmpty(laborFunction, "Labor Function");
                location = checkAndReplaceIfEmpty(location, "Location");
                postedDate = checkAndReplaceIfEmpty(postedDate, "Posted Date");
                description = checkAndReplaceIfEmpty(description, "Description");
                tagsString = checkAndReplaceIfEmpty(tagsString, "Tags");

                System.out.println("Job Page URL on jobs.techstars.com: " + absoluteJobPageTechstarsUrl);
                System.out.println("Position Name: " + positionName);
                System.out.println("URL to Organization: " + organizationUrl);
                System.out.println("Logo (Link): " + logoUrl);
                System.out.println("Organization Title: " + organizationTitle);
                System.out.println("Labor Function: " + laborFunction);
                System.out.println("Location: " + location);
                System.out.println("Posted Date (Unix Timestamp): " + postedDate);
                System.out.println("Description: " + description);
                System.out.println("Tags: " + tagsString);
                System.out.println("---------------------------------------------");

                Job newJob = new Job();
                newJob.setJobPageUrl(absoluteJobPageTechstarsUrl);
                newJob.setPositionName(positionName);
                newJob.setOrganizationUrl(organizationUrl);
                newJob.setLogoUrl(logoUrl);
                newJob.setOrganizationTitle(organizationTitle);
                newJob.setLaborFunction(laborFunction);
                newJob.setLocation(location);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(postedDate, formatter);
                long unixTimestamp = localDate.atStartOfDay(ZoneOffset.UTC).toEpochSecond();
                newJob.setPostedDate(Instant.ofEpochSecond(unixTimestamp));

                newJob.setDescription(description);
                newJob.setTagNames(tagsString);

                jobRepository.save(newJob);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String checkAndReplaceIfEmpty(String value, String fieldName) {
        return value.isEmpty() ? "NOT_FOUND for " + fieldName : value;
    }

    private String getDescriptionFromUrl(String jobPageUrl) {
        try {
            Document jobDocument = Jsoup.connect(jobPageUrl).get();
            Element descriptionElement = jobDocument.selectFirst(".sc-beqWaB.krsgIM");

            if (descriptionElement != null) {
                return descriptionElement.text();
            } else {
                return "Description not found";
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to fetch description";
        }
    }

    private String getFilteredLaborFunctionFromUrl(String jobPageUrl) {
        try {
            Document jobDocument = Jsoup.connect(jobPageUrl).get();
            Elements laborFunctionElements = jobDocument.select("div.sc-beqWaB.ekesvO p.sc-beqWaB.bpXRKw");

            StringBuilder laborFunctionBuilder = new StringBuilder();
            for (int i = 1; i < laborFunctionElements.size(); i++) {
                laborFunctionBuilder.append(laborFunctionElements.get(i).text()).append(" · ");
            }

            String laborFunction = laborFunctionBuilder.toString();
            return laborFunction.isEmpty() ? "Labor Function not found" : laborFunction.substring(0, laborFunction.length() - 3);
        } catch (IOException e) {
            e.printStackTrace();
            return "Failed to fetch labor function";
        }
    }

    private String getAbsoluteUrl(String baseUrl, String relativeUrl) {
        try {
            URI uri = new URI(relativeUrl);
            if (uri.isAbsolute()) {
                return relativeUrl;
            } else {
                URI baseUri = new URI(baseUrl);
                return baseUri.resolve(uri).toString();
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return relativeUrl;
    }

    public void createCSVFileFromDatabase() {
        List<Job> jobs = jobRepository.findAll();

        String[] header = { "ID", "JobPageURL", "PositionName", "OrganizationURL", "LogoURL", "OrganizationTitle",
                "LaborFunction", "Location", "PostedDate", "Description", "TagNames" };

        try (CSVWriter writer = new CSVWriter(new FileWriter(new File("database_data.csv")))) {
            writer.writeNext(header);

            for (Job job : jobs) {
                String[] data = {
                        String.valueOf(job.getId()),
                        job.getJobPageUrl(),
                        job.getPositionName(),
                        job.getOrganizationUrl(),
                        job.getLogoUrl(),
                        job.getOrganizationTitle(),
                        job.getLaborFunction(),
                        job.getLocation(),
                        job.getPostedDate().toString(),
                        job.getDescription(),
                        job.getTagNames()
                };
                writer.writeNext(data);
            }
            System.out.println("CSV file created successfully in the root directory of your application.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

