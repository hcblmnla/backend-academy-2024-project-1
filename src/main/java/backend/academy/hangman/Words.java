package backend.academy.hangman;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.jspecify.annotations.NonNull;

/**
 * Monad dictionary.
 *
 * @author alnmlbch
 */
public final class Words {

    /**
     * All category list.
     */
    public static final List<String> CATEGORIES = getCategoryStream()
        .map(Category::name)
        .sorted()
        .toList();

    private final List<Category> buffer = new ArrayList<>();
    private Category currentCategory;

    private Words() {
        // utility class
    }

    /**
     * Basic dictionary query.
     *
     * @return a list of words of this category and difficulty.
     */
    public static List<Unit> getUnitsBy(
        @NonNull final String categoryName,
        @NonNull final Difficulty difficulty
    ) {
        return getCategoryStream()
            .filter(category -> categoryName.equalsIgnoreCase(category.name()))
            .map(Category::units)
            .flatMap(Collection::stream)
            .filter(unit -> difficulty.test(unit.word))
            .toList();
    }

    @SuppressWarnings("MultipleStringLiterals")
    private static Stream<Category> getCategoryStream() {
        return new Words()

            .category("animals")
            .add("cat", "meow meow")
            .add("dog", "bark bark")
            .add("elephant", "big and grey")
            .add("lion", "king of the jungle")
            .add("owl", "wise night bird")
            .add("tiger", "striped big cat")
            .add("horse", "used to ride")
            .add("giraffe", "tallest animal")
            .add("kangaroo", "jumps and has a pouch")
            .add("zebra", "striped horse-like animal")
            .add("penguin", "flightless bird")
            .add("dolphin", "intelligent marine mammal")
            .add("shark", "large predatory fish")
            .add("whale", "largest marine mammal")
            .add("rabbit", "small and hops")
            .add("frog", "amphibian, lives in water and land")
            .add("bear", "large mammal, loves honey")
            .add("snake", "legless reptile")
            .add("crocodile", "large aquatic reptile")
            .add("hippopotamus", "large semi-aquatic mammal")
            .add("fox", "sly and cunning animal")
            .add("wolf", "ancestor of the domestic dog")
            .add("deer", "graceful herbivore with antlers")
            .add("bat", "only mammal capable of true flight")
            .add("parrot", "colorful bird that can mimic speech")
            .add("octopus", "intelligent marine creature with eight arms")
            .add("jellyfish", "transparent sea creature with tentacles")
            .add("beaver", "known for building dams")
            .add("bison", "large, shaggy-haired wild ox")
            .add("koala", "tree-dwelling marsupial from Australia")

            .category("science")
            .add("ion", "charged atom or molecule")
            .add("atom", "smallest unit of matter")
            .add("gravity", "force that pulls objects to Earth")
            .add("evolution", "theory by Darwin")
            .add("photosynthesis", "plants make food using sunlight")
            .add("neuron", "basic unit of the brain")
            .add("quantum", "smallest amount of a physical quantity")
            .add("molecule", "two or more atoms bonded together")
            .add("bacteria", "single-celled microorganisms")
            .add("virus", "infectious agent requiring a host")
            .add("galaxy", "system of stars, planets, and other matter")
            .add("black hole", "region of space with strong gravitational pull")
            .add("enzyme", "protein that catalyzes chemical reactions")
            .add("oxygen", "element essential for respiration")
            .add("eclipse", "when one celestial body moves into the shadow of another")
            .add("chromosome", "structure of nucleic acids and proteins")
            .add("volcano", "rupture in Earth's crust that spews lava")
            .add("telescope", "instrument to observe distant objects")
            .add("gene", "unit of heredity in living organisms")
            .add("antibody", "protein used by the immune system to neutralize pathogens")
            .add("planet", "celestial body orbiting a star")
            .add("neutrino", "subatomic particle with very low mass")
            .add("asteroid", "small rocky body orbiting the sun")
            .add("mutation", "change in the DNA sequence")
            .add("crystal", "solid material with atoms arranged in a regular pattern")
            .add("plasma", "fourth state of matter")
            .add("dark matter", "matter that does not emit or interact with electromagnetic radiation")
            .add("supernova", "explosion of a star")
            .add("nanotechnology", "science of manipulating materials on an atomic scale")
            .add("fusion", "process of combining two atomic nuclei to release energy")

            .category("programming")
            .add("algorithm", "step-by-step procedure for solving a problem")
            .add("variable", "placeholder for a value")
            .add("recursion", "function that calls itself")
            .add("compiler", "translates code to machine language")
            .add("inheritance", "OOP concept to inherit properties")
            .add("polymorphism", "ability to take many forms")
            .add("encapsulation", "bundling data with methods")
            .add("function", "block of code that performs a specific task")
            .add("exception", "error that occurs during execution")
            .add("array", "collection of elements in a specific order")
            .add("database", "organized collection of data")
            .add("interface", "a contract that defines methods")
            .add("constructor", "method to initialize an object")
            .add("framework", "a platform for developing software applications")
            .add("library", "collection of precompiled routines")
            .add("thread", "smallest unit of execution in a process")
            .add("API", "set of functions for interacting with a software component")
            .add("pointer", "variable that stores the memory address of another variable")
            .add("syntax", "rules that define the structure of a language")
            .add("bit", "smallest unit of data in a computer system")
            .add("boolean", "data type with two possible values: true or false")
            .add("hashmap", "data structure that implements an associative array")
            .add("lambda", "anonymous function")
            .add("module", "self-contained unit of code")
            .add("protocol", "set of rules governing data exchange")
            .add("JSON", "lightweight data-interchange format")
            .add("IDE", "Integrated Development Environment for writing code")

            .category("fp")
            .add("immutable", "data that cannot be changed once created")
            .add("monad", "a design pattern used to handle program-wide concerns like state or IO")
            .add("functor", "a type that implements map or fmap to apply a function over wrapped values")
            .add("lazy", "delaying the evaluation of an expression until its value is actually needed")
            .add("recursion", "the process in which a function calls itself as a subroutine")
            .add("map", "a function that applies a given function to each item of a collection")
            .add("filter", "a function that selects elements from a collection based on a predicate")
            .add("reduce", "a function that processes a list and returns a single cumulative value")
            .add("closure", "a function that captures the bindings of its free variables")
            .add("composition", "the process of combining two or more functions to produce a new function")
            .add("memoization", "an optimization technique that stores the results of expensive function calls")
            .add("generator", "a function that returns an iterator which yields a sequence of values")
            .add("combinator", "a function that builds a more complex function out of simpler ones")
            .add("bifunctor", "a functor with two type parameters, both of which can be mapped over")
            .add("catamorphism", "a generalization of folding or reducing data structures")
            .add("homomorphism", "a structure-preserving map between two algebraic structures")

            .buffer.stream();
    }

    private Words category(final String name) {
        currentCategory = new Category(name);
        buffer.add(currentCategory);
        return this;
    }

    private Words add(final String word, final String hint) {
        currentCategory.add(word, hint);
        return this;
    }

    @Getter
    private static final class Category {
        private final String name;
        private final List<Unit> units = new ArrayList<>();

        private Category(@NonNull final String name) {
            this.name = name;
        }

        private void add(@NonNull final String word, final String hint) {
            units.add(Unit.of(word, hint));
        }
    }

    /**
     * A pair of words and hints to the word.
     */
    public record Unit(@NonNull String word, String hint) {
        public static Unit of(@NonNull final String word, final String hint) {
            return new Unit(word, hint);
        }
    }
}
