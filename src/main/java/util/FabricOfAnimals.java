package util;

import IslandStructure.Island;
import IslandStructure.Location;
import IslandStructure.Statistics;
import entities.Entity;
import entities.Constants;
import entities.Organisms;
import lombok.Getter;
import org.apache.logging.log4j.core.Logger;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

@Getter
public class FabricOfAnimals {



    Constants constants;

    Island island;
    Statistics statistics;
    Lock lockFabric;


    private final ConcurrentHashMap<Organisms, ArrayList<Entity>> poolAnimals;

    private final HashMap<Organisms, Entity> mapOfFounders = new HashMap<>();

    public FabricOfAnimals(Constants constants, Island island, Statistics statistics) {
        this.constants = constants;
        this.island = island;
        this.statistics = statistics;
        for (Organisms TYPE : Organisms.values()
        ) {
            mapOfFounders.put(TYPE, new Entity(
                    constants.getMaxWeight().get(TYPE),
                    constants.getSPEED().get(TYPE),
                    constants.getSATURATION().get(TYPE),
                    TYPE,
                    constants.getICON().get(TYPE),
                    constants.getMapOfFood().get(TYPE)));
        }
        poolAnimals = new ConcurrentHashMap<>() {
            {
                put(Organisms.WOLF, new ArrayList<>());
                put(Organisms.BOA, new ArrayList<>());
                put(Organisms.FOX, new ArrayList<>());
                put(Organisms.BEAR, new ArrayList<>());
                put(Organisms.EAGLE, new ArrayList<>());
                put(Organisms.HORSE, new ArrayList<>());
                put(Organisms.DEER, new ArrayList<>());
                put(Organisms.RABBIT, new ArrayList<>());
                put(Organisms.MOUSE, new ArrayList<>());
                put(Organisms.GOAT, new ArrayList<>());
                put(Organisms.SHEEP, new ArrayList<>());
                put(Organisms.BOAR, new ArrayList<>());
                put(Organisms.BUFFALO, new ArrayList<>());
                put(Organisms.DUCK, new ArrayList<>());
                put(Organisms.CATERPILLAR, new ArrayList<>());
                put(Organisms.PLANT, new ArrayList<>());
            }
        };
    }


    public void createNewAnimals(Location location, Organisms TYPE, Statistics statistics) {
        try {
            if (location.getLock().tryLock(30, TimeUnit.MILLISECONDS)) {
                if (location.getCountAnimalsMapOnLocation().get(TYPE) < constants.getMaxAnimalForKindOfLocations().get(TYPE)) {
                    if (!poolAnimals.get(TYPE).isEmpty()) {
                        location.getAnimalsIn().add(poolAnimals.get(TYPE).remove(0));
                    } else {
                        Entity entity = mapOfFounders.get(TYPE).clone();
                        entity.name = entity.getTYPE().name() + statistics.getStatistics().get(TYPE);
                        entity.weight = ThreadLocalRandom.current().nextDouble(constants.getMaxWeight().get(TYPE) / 2, constants.getMaxWeight().get(TYPE));
                        location.getAnimalsIn().add(entity);

                    }
                    statistics.getStatistics().replace(TYPE, statistics.getStatistics().get(TYPE) + 1);
                    location.getCountAnimalsMapOnLocation().replace(TYPE, location.getCountAnimalsMapOnLocation().get(TYPE) + 1);
                }
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            location.getLock().unlock();
        }
    }
}

//Запуск создания животных на всех локациях(нужно сделать одновременный запуск для всех локаций)


