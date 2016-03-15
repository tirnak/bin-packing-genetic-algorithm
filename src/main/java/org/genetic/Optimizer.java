package org.genetic;

import org.model.Box;
import org.apache.commons.math3.genetics.*;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by kise0116 on 14.03.2016.
 */
public class Optimizer {

    private static final int DIMENSION = 50;
    private static final int POPULATION_SIZE = 50;
    private static final int NUM_GENERATIONS = 100;
    private static final double ELITISM_RATE = 0.2;
    private static final double CROSSOVER_RATE = 1;
    private static final double MUTATION_RATE = 0.1;
    private static final int TOURNAMENT_ARITY = 5;

    public static List<Box> main(List<Box> boxes) {// initialize a new org.genetic algorithm
        GeneticAlgorithm ga = new GeneticAlgorithm(
                new OrderedCrossover<>(),
                CROSSOVER_RATE,
                new MoveToHeadMutation(),
                MUTATION_RATE,
                new TournamentSelection(TOURNAMENT_ARITY)
        );

        Population initial = getInitialPopulation(boxes);

        StoppingCondition stopCond = new FixedGenerationCount(NUM_GENERATIONS);

        Population finalPopulation = ga.evolve(initial, stopCond);

        return ((BoxChromosome)finalPopulation.getFittestChromosome()).getRepresentation();
    }

    private static Population getInitialPopulation(List<Box> boxes) {
        List<Chromosome> popList = new LinkedList<>();

        for (int i=0; i<POPULATION_SIZE; i++) {
                List<Box> newSequence = boxes.stream().map(Box::CloneNonApi).collect(Collectors.toList());
            Collections.shuffle(newSequence);
            popList.add(new BoxChromosome(newSequence));
        }
        return new ElitisticListPopulation(popList, popList.size(), ELITISM_RATE);
    }

}
