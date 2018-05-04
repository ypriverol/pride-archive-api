package uk.ac.ebi.pride.ws.pride.models.dataset;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.*;


import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.Set;

/**
 * @author ypriverol
 */

@Data
@Builder
@JsonRootName("dataset")
@XmlRootElement(name = "dataset")
public class CompactDataset implements Serializable {

    @XmlElement
    private String accession;
    private String title;
    private Collection<String> additionalAttributes;
    private String projectDescription;
    private String sampleProcessingProtocol;
    private String dataProcessingProtocol;
    private Collection<String> projectTags;
    private Collection<String> keywords;
    private String doi;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date submissionDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date publicationDate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date updatedDate;
    private Collection<String> submitters;
    private Collection<String> labPIs;
    private Collection<String> affiliations;
    private Collection<String> instruments;
    private Collection<String> softwares;
    private Collection<String> quantificationMethods;
    private Set<String> countries;
    private Collection<String> sampleAttributes;
    private Collection<String> organisms;
    private Collection<String> organismParts;
    private Collection<String> diseases;
    private Set<String> references;
    private Set<String> identifiedPTMStrings;
}
