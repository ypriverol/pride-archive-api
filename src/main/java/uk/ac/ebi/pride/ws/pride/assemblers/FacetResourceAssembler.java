package uk.ac.ebi.pride.ws.pride.assemblers;

import org.apache.commons.lang3.tuple.ImmutableTriple;
import org.apache.commons.lang3.tuple.Triple;
import org.assertj.core.groups.Tuple;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetAndHighlightPage;
import org.springframework.data.solr.core.query.result.FacetEntry;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import uk.ac.ebi.pride.solr.indexes.pride.model.PrideSolrDataset;
import uk.ac.ebi.pride.ws.pride.hateoas.Facet;
import uk.ac.ebi.pride.ws.pride.hateoas.Facets;
import uk.ac.ebi.pride.ws.pride.models.dataset.FacetResource;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author ypriverol
 */
public class FacetResourceAssembler extends ResourceAssemblerSupport<PrideSolrDataset, FacetResource> {

    public FacetResourceAssembler(Class<?> controller, Class<FacetResource> resourceType) {
        super(controller, resourceType);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<FacetResource> toResources(Iterable<? extends PrideSolrDataset> entities) {

        List<FacetResource> facets = new ArrayList<>();
        FacetPage<PrideSolrDataset> facetPages;

        if(entities instanceof FacetAndHighlightPage){
            facetPages = (FacetPage<PrideSolrDataset>) entities;
            Map<String, List<FacetEntry>> values = new HashMap<>();
            for(Page<? extends FacetEntry> facet: facetPages.getAllFacets()){
               values.putAll(facet.getContent().stream().collect(Collectors.groupingBy(entry -> entry.getKey().toString())));
            }
            facets = values.entrySet().stream().map( x-> new FacetResource(new Facets(x.getKey(), x.getValue().stream().map(xValue -> new Facet(xValue.getValue(), xValue.getValueCount())).collect(Collectors.toList())), new ArrayList<>())).collect(Collectors.toList());
        }
        return facets;

    }


    @Override
    public FacetResource toResource(PrideSolrDataset prideSolrDataset) {
        return null;
    }}