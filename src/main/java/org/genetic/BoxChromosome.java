package org.genetic;

import org.Calculator;
import org.model.Box;
import org.apache.commons.math3.exception.util.LocalizedFormats;
import org.apache.commons.math3.genetics.AbstractListChromosome;
import org.apache.commons.math3.genetics.InvalidRepresentationException;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kise0116 on 14.03.2016.
 */
public class BoxChromosome extends AbstractListChromosome<Box> {

    /**
     * Constructor.
     * @param representation inner representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public BoxChromosome(final List<Box> representation) throws InvalidRepresentationException {
        super(representation);
    }

    /**
     * Constructor.
     * @param representation inner representation of the chromosome
     * @throws InvalidRepresentationException iff the <code>representation</code> can not represent a valid chromosome
     */
    public BoxChromosome(final Box[] representation) throws InvalidRepresentationException {
        super(representation);
    }

    @Override
    protected void checkValidity(List<Box> list) throws InvalidRepresentationException {
        if (!list.stream().allMatch(b -> b != null)) {
            throw new InvalidRepresentationException(LocalizedFormats.ARGUMENT_OUTSIDE_DOMAIN);
        };
    }

    @Override
    public AbstractListChromosome<Box> newFixedLengthChromosome(List<Box> list) {
        return new BoxChromosome(list);
    }

    /**
     * deeply copies representation
     * @return
     */
    @Override
    public List<Box> getRepresentation() {
        return super.getRepresentation()
                .stream().map(Box::CloneNonApi)
                .collect(Collectors.toList());
    }

    @Override
    public double fitness() {
        Calculator calculator = Calculator._instance;
        return calculator.calculateAndUnset(getRepresentation());
    }
}
