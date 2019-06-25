package uk.ac.ebi.pride.ws.pride.assemblers.molecules;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import uk.ac.ebi.pride.archive.dataprovider.param.CvParamProvider;
import uk.ac.ebi.pride.mongodb.archive.model.PrideArchiveField;
import uk.ac.ebi.pride.mongodb.molecules.model.protein.PrideMongoProteinEvidence;
import uk.ac.ebi.pride.ws.pride.controllers.molecules.PeptideEvidenceController;
import uk.ac.ebi.pride.ws.pride.controllers.molecules.ProteinEvidenceController;
import uk.ac.ebi.pride.ws.pride.models.molecules.IdentifiedModification;
import uk.ac.ebi.pride.ws.pride.models.molecules.ProteinEvidence;
import uk.ac.ebi.pride.ws.pride.models.molecules.ProteinEvidenceResource;
import uk.ac.ebi.pride.ws.pride.models.param.CvParam;
import uk.ac.ebi.pride.ws.pride.transformers.Transformer;
import uk.ac.ebi.pride.ws.pride.utils.WsContastants;
import uk.ac.ebi.pride.ws.pride.utils.WsUtils;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ProteinEvidenceAssembler extends ResourceAssemblerSupport<PrideMongoProteinEvidence, ProteinEvidenceResource> {

    public ProteinEvidenceAssembler(Class<?> controllerClass, Class<ProteinEvidenceResource> resourceType) {
        super(controllerClass, resourceType);
    }

    @Override
    public ProteinEvidenceResource toResource(PrideMongoProteinEvidence prideMongoProteinEvidence) {

        List<Link> links = new ArrayList<>();
        links.add(ControllerLinkBuilder.linkTo(
                ControllerLinkBuilder.methodOn(ProteinEvidenceController.class)
                        .getProteinEvidence(WsUtils.getIdentifier(prideMongoProteinEvidence.getProjectAccession(),
                                prideMongoProteinEvidence.getAssayAccession(),
                                prideMongoProteinEvidence.getReportedAccession())))
                .withSelfRel());

        Method method = null;
        try {
            method = PeptideEvidenceController.class.getMethod("getPeptideEvidencesByProteinEvidence",
                    String.class, String.class, String.class,
                    Integer.class, Integer.class,
                    String.class, String.class);
            Link link = ControllerLinkBuilder.linkTo(method, prideMongoProteinEvidence.getReportedAccession(),
                    prideMongoProteinEvidence.getProjectAccession(), prideMongoProteinEvidence.getAssayAccession(),
                    WsContastants.MAX_PAGINATION_SIZE, 0,
                    "DESC" , PrideArchiveField.EXTERNAL_PROJECT_ACCESSION)
                    .withRel(WsContastants.HateoasEnum.peptideevidences.name());
            links.add(link);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        return new ProteinEvidenceResource(transform(prideMongoProteinEvidence), links);
    }

    /**
     * Transform a {@link PrideMongoProteinEvidence} into a {@link ProteinEvidence}
     * @param prideMongoProteinEvidence Mongo protein evidence
     * @return A {@link ProteinEvidence}
     */
    private ProteinEvidence transform(PrideMongoProteinEvidence prideMongoProteinEvidence) {
        List<CvParam> additionalAttributes = prideMongoProteinEvidence.getAdditionalAttributes()
                .stream()
                .map( x-> new CvParam(x.getCvLabel(), x.getAccession(), x.getName(), x.getValue()))
                .collect(Collectors.toList());

        CvParam bestSearchEngine = null;
        if(prideMongoProteinEvidence.getBestSearchEngineScore() != null){
            bestSearchEngine = new CvParam(prideMongoProteinEvidence.getBestSearchEngineScore().getCvLabel(),
                    prideMongoProteinEvidence.getBestSearchEngineScore().getAccession(),
                    prideMongoProteinEvidence.getBestSearchEngineScore().getName(),
                    prideMongoProteinEvidence.getBestSearchEngineScore().getValue());
        }
        List<IdentifiedModification> ptms = new ArrayList<>();
        if(prideMongoProteinEvidence.getPtms() != null){
            ptms = Transformer.transformModifications(prideMongoProteinEvidence.getPtms());
        }

        return ProteinEvidence.builder()
                .usi(WsUtils.getIdentifier(prideMongoProteinEvidence.getProjectAccession(),
                        prideMongoProteinEvidence.getAssayAccession(),
                        prideMongoProteinEvidence.getReportedAccession()))
                .reportedAccession(prideMongoProteinEvidence.getReportedAccession())
                .assayAccession(prideMongoProteinEvidence.getAssayAccession())
                .projectAccession(prideMongoProteinEvidence.getProjectAccession())
                .proteinDescription(prideMongoProteinEvidence.getProteinDescription())
                .proteinGroupMembers(prideMongoProteinEvidence.getProteinGroupMembers())
                .proteinSequence(prideMongoProteinEvidence.getProteinSequence())
                .additionalAttributes(additionalAttributes)
                .bestSearchEngineScore(bestSearchEngine)
                .isValid(prideMongoProteinEvidence.getIsValid())
                .qualityMethods(prideMongoProteinEvidence.getQualityEstimationMethods()
                        .stream()
                        .map( x-> new CvParam(((CvParamProvider) x).getCvLabel(), ((CvParamProvider) x).getAccession(),
                                ((CvParamProvider) x).getName(), ((CvParamProvider) x).getValue()))
                        .collect(Collectors.toList()))
                .ptms(ptms)
                .build();
    }
}
