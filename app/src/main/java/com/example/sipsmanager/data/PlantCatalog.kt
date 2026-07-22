package com.example.sipsmanager.data

import com.example.sipsmanager.types.PlantType
import com.example.sipsmanager.types.PlantLayout
import com.example.sipsmanager.types.GrowthStage
import com.example.sipsmanager.types.DiagramPoint

object PlantCatalog {
    val EXHAUSTIVE_PLANT_CATALOG = listOf(
        PlantType(
            id = "cherry_tomato",
            name = "Cherry Tomato",
            scientificName = "Solanum lycopersicum var. cerasiforme",
            category = "Vegetable",
            description = "Sweet, productive, and fast-growing tomato variety. Excellent for small space container gardening and a classic favorite for Earthbox. Must be grown with an Earthbox standard trellis or dynamic frame support.",
            climbingFrameRequired = true,
            climbingFrameType = "Cage/Trellis Frame (Standard Sturdy Cage or Staking Rail)",
            shadeRequirement = "Full Sun",
            waterConsumption = "Extreme",
            waterGallonsPerWeekLocal = 6.5,
            daysToHarvest = 65,
            daysToSprout = 7,
            recommendedPlantingSeason = "Spring (Post last-frost) to Early Summer",
            earthboxLayout = PlantLayout(
                rows = 1,
                plantsPerRow = 2,
                totalPlants = 2,
                fertilizerBand = "side",
                diagramPoints = listOf(
                    DiagramPoint(25f, 50f),
                    DiagramPoint(75f, 50f)
                ),
                instructions = "Plant exactly 2 tomato starts in the center of the box along the length. Place your 2-cup fertilizer band along the opposite side from the water tube."
            ),
            stages = listOf(
                GrowthStage(1, 10, "Seedling Sprout", "First cotyledon leaves emerge from the soil. Taproot accesses moisture reservoir.", "Keep soil moist around stem. Ensure maximum overhead sunlight to prevent leggy growth.", "cotyledon", 2),
                GrowthStage(11, 25, "Early Vegetative", "True jagged tomato leaves form. The branch system begins extending, and main stalk thickens.", "Fasten gently to the lower rungs of the trellis or climbing frame using soft garden twine.", "veg_early", 8),
                GrowthStage(26, 45, "Flowering Stage", "Yellow blossoms emerge. First signs of minor side shoots (suckers) appearing in leaf axils.", "Prune select suckers below the first blossom cluster to direct energy upward. Gently shake vines to aid pollination.", "flower", 22),
                GrowthStage(46, 65, "Harvest Maturity", "Clusters of spherical cherry tomatoes change from pale green, to orange, and finally deep ruby red.", "Keep reservoir topped off daily as extreme water usage peak occurs. Pick daily to encourage continuous production.", "fruit", 42)
            )
        ),
        PlantType(
            id = "bell_pepper",
            name = "Bell Pepper",
            scientificName = "Capsicum annuum",
            category = "Vegetable",
            description = "Sweet bell peppers do remarkably well in the double-row layout. They produce crisp, thick-walled fruits and require minimal pruning.",
            climbingFrameRequired = false,
            climbingFrameType = "Single support stakes to handle heavy fruit payload if desired",
            shadeRequirement = "Full Sun",
            waterConsumption = "High",
            waterGallonsPerWeekLocal = 4.8,
            daysToHarvest = 75,
            daysToSprout = 10,
            recommendedPlantingSeason = "Late Spring / Warm Soils",
            earthboxLayout = PlantLayout(
                rows = 2,
                plantsPerRow = 3,
                totalPlants = 6,
                fertilizerBand = "center",
                diagramPoints = listOf(
                    DiagramPoint(20f, 25f), DiagramPoint(50f, 25f), DiagramPoint(80f, 25f),
                    DiagramPoint(20f, 75f), DiagramPoint(50f, 75f), DiagramPoint(80f, 75f)
                ),
                instructions = "Plant 6 peppers in two parallel rows of three. Place the fertilizer band exactly down the center, between the two rows."
            ),
            stages = listOf(
                GrowthStage(1, 12, "Tiny Shoot", "Two broad, shiny heart-shaped seed leaves break the mulch casing.", "Protect from hard driving rains. Maintain warm moisture in the upper soil.", "cot_pepper", 1),
                GrowthStage(13, 30, "Robust Canopy", "Broad dark-green leaves stack in pairs forming a compact, dense bush shape.", "Mulch properly around the planting slots to preserve moisture. Do not pinch the main grow tip.", "veg_pepper", 7),
                GrowthStage(31, 55, "White Blossoms", "Star-shaped white flowers point downwards, preparing to drop petals and push peppers out.", "Water must remain consistent. Drought now will cause blossom end rot or flower drop.", "flower_pepper", 15),
                GrowthStage(56, 75, "Sweet Harvesting", "Peppers expand, turning dark glossy green and shifting to vibrant yellow or sweet red.", "Snip bell peppers cleanly with shears rather than pulling, to avoid breaking fragile branches.", "fruit_pepper", 24)
            )
        ),
        PlantType(
            id = "slicing_cucumber",
            name = "Slicing Cucumber",
            scientificName = "Cucumis sativus",
            category = "Vegetable",
            description = "Cucumbers grow rapidly and climb heavily. Highly efficient in sub-irrigated setups as they are water-intensive and prone to yellowing on standard irrigation.",
            climbingFrameRequired = true,
            climbingFrameType = "Climbing Trellis (Absolute Necessity - Vines climb rapidly via tendrils)",
            shadeRequirement = "Full Sun",
            waterConsumption = "High",
            waterGallonsPerWeekLocal = 5.5,
            daysToHarvest = 55,
            daysToSprout = 5,
            recommendedPlantingSeason = "Spring or Late Summer",
            earthboxLayout = PlantLayout(
                rows = 1,
                plantsPerRow = 4,
                totalPlants = 4,
                fertilizerBand = "side",
                diagramPoints = listOf(
                    DiagramPoint(15f, 50f),
                    DiagramPoint(38f, 50f),
                    DiagramPoint(62f, 50f),
                    DiagramPoint(85f, 50f)
                ),
                instructions = "Plant 4 cucumber starts in a single row along the centerline. Direct their initial growth towards the trellis structure."
            ),
            stages = listOf(
                GrowthStage(1, 7, "Fast Cotyledon", "Large, thick, paddle-shaped seed leaves push open the soil cover dramatically.", "Ensure birds do not pluck the fresh fleshy seedlings.", "cot_cuke", 2),
                GrowthStage(8, 20, "Tendril Expansion", "Rough, sandpaper-textured leaves unfurl as thin, spiral-seeking search tendrils twist into the air.", "Guide the first tendrils manually onto your Earthbox trellis mesh to train them upward.", "veg_cuke", 10),
                GrowthStage(21, 40, "Golden Flowers", "Flourishing vines produce numerous yellow flowers. Female blossoms have miniature cucumbers at their base.", "Maintain water reservoir capacity. Bees must have access to pollinate male and female flowers.", "flower_cuke", 28),
                GrowthStage(41, 55, "Crisp Picking", "Heavy cylindrical dark-green cucumbers hang from vines. High water levels ensure non-bitter skin.", "Harvest regularly. Leaving massive cucumbers on the vine shuts down further flower production.", "fruit_cuke", 50)
            )
        ),
        PlantType(
            id = "sweet_basil",
            name = "Sweet Basil",
            scientificName = "Ocimum basilicum",
            category = "Herb",
            description = "An aromatic delight that thrives inside sub-irrigated system. Constant moisture from bottom-up delivery develops rich, lush foliage with high oil content.",
            climbingFrameRequired = false,
            shadeRequirement = "Partial Shade",
            waterConsumption = "Moderate",
            waterGallonsPerWeekLocal = 3.2,
            daysToHarvest = 45,
            daysToSprout = 6,
            recommendedPlantingSeason = "Late Spring through Summer",
            earthboxLayout = PlantLayout(
                rows = 2,
                plantsPerRow = 3,
                totalPlants = 6,
                fertilizerBand = "center",
                diagramPoints = listOf(
                    DiagramPoint(20f, 25f), DiagramPoint(50f, 25f), DiagramPoint(80f, 25f),
                    DiagramPoint(20f, 75f), DiagramPoint(50f, 75f), DiagramPoint(80f, 75f)
                ),
                instructions = "Plant 6 basil starts in two rows. Place the fertilizer band exactly down the center, between the rows."
            ),
            stages = listOf(
                GrowthStage(1, 8, "Aromatic Shoot", "Perfect rounded seedlings appear, giving off a tiny whiff of spice when touched.", "Water gently, avoid wetting the foliage excessively during midday hot sun.", "cot_basil", 1),
                GrowthStage(9, 22, "Branching Bush", "Spoon-shaped glossy basil leaves emerge. Compact branching occurs as node distances stay tight.", "Pinch off the top set of leaves when the seedling has 3 to 4 nodes to force bushy lateral growth.", "veg_basil", 5),
                GrowthStage(23, 35, "Lush Foliage", "Dense canopy of beautiful edible leaves. Stems thicken and woodiness is prevented by constant picking.", "Pinch any flower stalks immediately. Basil flower heads make the leaves bitter and stop vegetative growth.", "flower_basil", 12),
                GrowthStage(36, 45, "Continuous Harvest", "A massive, fragrant bush ready for continuous pruning. Excellent for fresh pesto or culinary plates.", "Always harvest leaves from the top down, cutting above a leaf node where two small shoots are growing.", "fruit_basil", 18)
            )
        ),
        PlantType(
            id = "japanese_eggplant",
            name = "Japanese Eggplant",
            scientificName = "Solanum melongena",
            category = "Vegetable",
            description = "Slender, sweet, non-bitter eggplants that thrive on warm, constant moisture. Highly attractive purple foliage and star-shaped lavender blossoms make it highly ornamental.",
            climbingFrameRequired = false,
            climbingFrameType = "Inconspicuous wooden stake or stakes to support heavy fruits later",
            shadeRequirement = "Full Sun",
            waterConsumption = "High",
            waterGallonsPerWeekLocal = 4.5,
            daysToHarvest = 65,
            daysToSprout = 8,
            recommendedPlantingSeason = "Spring (Post last-frost)",
            earthboxLayout = PlantLayout(
                rows = 1,
                plantsPerRow = 2,
                totalPlants = 2,
                fertilizerBand = "side",
                diagramPoints = listOf(
                    DiagramPoint(25f, 50f),
                    DiagramPoint(75f, 50f)
                ),
                instructions = "Plant exactly 2 eggplants in the centerline. Place your fertilizer strip along the opposite side from the water tube."
            ),
            stages = listOf(
                GrowthStage(1, 9, "Sprout Phase", "Lance-like seed leaves push up, quickly developing a subtle purple hue along their stems.", "Ensure warmth is maintained. Eggplants stunt easily under cold spring night dampness.", "cot_egg", 2),
                GrowthStage(10, 25, "Velvet Foliage", "Broad, velvety, deeply ribbed leaves sprout. The plant assumes a sturdy upright branching structure.", "Look out for early flea beetles. Consider soft organic soap spray if tiny holes appear on leaves.", "veg_egg", 7),
                GrowthStage(26, 48, "Lavender Stars", "Aesthetic violet, five-point blossoms appear. Golden yellow anthers glow in the center of the flowers.", "Flick blossoms gently with a finger to distribute pollen inside. Consistent water level prevents flower drop.", "flower_egg", 16),
                GrowthStage(49, 65, "Glossy Harvest", "Long, slim, purplish-black Japanese eggplants dangle gracefully. Surface skin glows like fine lacquer.", "Cut with hand shears leaving an inch of thorny cap attached. Do not pluck or tear the fruit stem.", "fruit_egg", 26)
            )
        ),
        PlantType(
            id = "romaine_lettuce",
            name = "Romaine Lettuce",
            scientificName = "Lactuca sativa var. longifolia",
            category = "Vegetable",
            description = "Crisp heads of delicious salad romaine. Exceptionally easy in an Earthbox. High density layout allows continuous harvest of outer baby leaves.",
            climbingFrameRequired = false,
            shadeRequirement = "Partial Shade",
            waterConsumption = "Low",
            waterGallonsPerWeekLocal = 2.2,
            daysToHarvest = 55,
            daysToSprout = 4,
            recommendedPlantingSeason = "Cool seasons (Early Autumn or Early Spring)",
            earthboxLayout = PlantLayout(
                rows = 2,
                plantsPerRow = 4,
                totalPlants = 8,
                fertilizerBand = "center",
                diagramPoints = listOf(
                    DiagramPoint(15f, 25f), DiagramPoint(38f, 25f), DiagramPoint(62f, 25f), DiagramPoint(85f, 25f),
                    DiagramPoint(15f, 75f), DiagramPoint(38f, 75f), DiagramPoint(62f, 75f), DiagramPoint(85f, 75f)
                ),
                instructions = "Plant 8 lettuce seedlings in parallel rows of four. Pour your fertilizer band precisely along the center line between rows."
            ),
            stages = listOf(
                GrowthStage(1, 7, "Delicate Green", "Delicate circular leaves emerge. Extremely fast to start in cooler moist soil.", "Shelter from intense mid-day heat. Lettuce seeds struggle above 85°F (30°C).", "cot_lettuce", 1),
                GrowthStage(8, 20, "Baby Rosette", "Ruffled outer leaves begin extending outward from a central dynamic crown.", "Check reservoir to ensure it is constantly damp but not muddy. Add organic fish fertilizer if growing pale.", "veg_lettuce", 4),
                GrowthStage(21, 40, "Crisp Heart Fold", "Foliage wraps tightly inward, creating the tall upright elongated head characteristic of romaine.", "Harvest outer leaves as baby greens anytime, or wait for the whole head to mature.", "flower_lettuce", 8),
                GrowthStage(41, 55, "Full Salad Head", "Dense, chunky, 10-inch columns of crunchy pale-green ribs and emerald leaf caps.", "Harvest in the early cool morning by slicing the head off at soil level. Morning harvest keeps leaves sweet.", "fruit_lettuce", 11)
            )
        ),
        PlantType(
            id = "strawberry",
            name = "Strawberry",
            scientificName = "Fragaria x ananassa",
            category = "Fruit",
            description = "Delicious everbearing strawberries. The unique double-row sub-irrigated Earthbox keeps the fruit dry and elevated away from mud, reducing decay.",
            climbingFrameRequired = false,
            shadeRequirement = "Full Sun",
            waterConsumption = "Moderate",
            waterGallonsPerWeekLocal = 3.8,
            daysToHarvest = 60,
            daysToSprout = 12,
            recommendedPlantingSeason = "Early Spring or Autumn setup",
            earthboxLayout = PlantLayout(
                rows = 2,
                plantsPerRow = 3,
                totalPlants = 6,
                fertilizerBand = "center",
                diagramPoints = listOf(
                    DiagramPoint(20f, 25f), DiagramPoint(50f, 25f), DiagramPoint(80f, 25f),
                    DiagramPoint(20f, 75f), DiagramPoint(50f, 75f), DiagramPoint(80f, 75f)
                ),
                instructions = "Plant 6 everbearing strawberry crowns. They will grow into a compact lush trailing visual mound."
            ),
            stages = listOf(
                GrowthStage(1, 12, "Runner Emergence", "Tender serrated trifoliate leaves start spreading from the central root crown.", "Do not bury the crown; keep it level with the soil line.", "cot_strawberry", 2),
                GrowthStage(13, 28, "Rosette Expansion", "Lush dark green clumps form, with short leaf stalks spreading outwards.", "Pinch early runners (long leafless stems) to direct plant energy into strong crown roots.", "veg_strawberry", 5),
                GrowthStage(29, 45, "White Petals", "Charming white five-petal flowers with dense yellow button centers open in clusters.", "Bees are key. Make sure the container is open to friendly garden insects.", "flower_strawberry", 8),
                GrowthStage(46, 60, "Sweet Ruby Berries", "Berry caps swell from green to light white, before flushing into glowing, sweet ruby red.", "Pick berries daily. Enjoy them fresh off the stems for highest sugar content.", "fruit_strawberry", 10)
            )
        )
    )

    fun getPlantById(id: String): PlantType? {
        return EXHAUSTIVE_PLANT_CATALOG.find { it.id == id }
    }
}
