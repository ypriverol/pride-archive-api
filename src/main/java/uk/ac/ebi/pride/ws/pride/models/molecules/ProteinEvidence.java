package uk.ac.ebi.pride.ws.pride.models.molecules;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.Builder;
import lombok.Data;
import org.springframework.hateoas.core.Relation;
import uk.ac.ebi.pride.archive.dataprovider.data.ptm.DefaultIdentifiedModification;
import uk.ac.ebi.pride.ws.pride.models.param.CvParam;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;
import java.util.Set;

@Data
@Builder
@XmlRootElement(name = "proteinevidence")
@JsonRootName("proteinevidence")
@JsonTypeName("proteinevidence")
@Relation(collectionRelation = "proteinevidences")
public class ProteinEvidence {

    String projectAccession;
    String assayAccession;
    String reportedAccession;
    String proteinSequence;
    String uniprotMappedProteinAccession;
    String ensemblMappedProteinAccession;
    Set<String> proteinGroupMembers;
    String proteinDescription;
    List<CvParam> additionalAttributes;
    List<IdentifiedModification> ptms;
    CvParam bestSearchEngineScore;
    private boolean isDecoy;
}
