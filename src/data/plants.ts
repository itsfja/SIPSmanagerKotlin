import { PlantType } from '../types';

export const EXHAUSTIVE_PLANT_CATALOG: PlantType[] = [
  {
    id: 'cherry_tomato',
    name: 'Super Sweet 100 Cherry Tomato',
    scientificName: 'Solanum lycopersicum',
    expectedYield: '8-10 lbs per plant',
    waterUsage: 'Very High',
    stages: [
      { name: 'Seedling Sprout', dayRangeMin: 0, dayRangeMax: 10, heightInches: 3, description: 'Cotyledons expand and the first true leaf cluster emerges. Capillary moisture from sub-irrigation should remain high to encourage rooting.', careTips: 'Keep plastic mulch cover tight to maintain 85% humidity.' },
      { name: 'Vegetative Vigor', dayRangeMin: 11, dayRangeMax: 30, heightInches: 12, description: 'Stems thicken rapidly and nodes branch out. The primary root system descends into the sub-surface moist air layer.', careTips: 'Install tomato cage or support stakes. Keep filler tube covered.' },
      { name: 'Blooming Yellows', dayRangeMin: 31, dayRangeMax: 50, heightInches: 28, description: 'Clusters of yellow flowers appear. Rapid potassium and calcium uptake occurs through the soil fertilizer band.', careTips: 'Tap flowers gently to aid wind self-pollination. Refill water daily.' },
      { name: 'Green Fruiting', dayRangeMin: 51, dayRangeMax: 70, heightInches: 42, description: 'Tiny green fruits emerge and swell. Water usage is at maximum peak.', careTips: 'Top off reservoir every morning. Check foliage for spider mites.' },
      { name: 'Bountiful Harvest', dayRangeMin: 71, dayRangeMax: 120, heightInches: 60, description: 'Lustrous red cherry tomatoes ripen in cascading trusses.', careTips: 'Harvest fully colored fruits daily. Prune bottom yellow leaves.' }
    ],
    earthboxLayout: {
      totalPlants: 2,
      fertilizerBand: 'side',
      diagramPoints: [
        { x: 25, y: 50 },
        { x: 75, y: 50 }
      ],
      instructions: 'Always plant exactly 2 cherry tomato starts on the side opposite the water filler tube. Pack a 2-cup strip of organic slow-release tomato fertilizer in a single continuous band along the back edge of the Earthbox.'
    }
  },
  {
    id: 'romaine_lettuce',
    name: 'Parris Island Cos Romaine Lettuce',
    scientificName: 'Lactuca sativa',
    expectedYield: '1 head per plant',
    waterUsage: 'Medium',
    stages: [
      { name: 'Tender Sprout', dayRangeMin: 0, dayRangeMax: 7, heightInches: 1.5, description: 'Delicate green leaf paddles appear above the moist potting medium.', careTips: 'Ensure even solar warming. Avoid direct heavy downpours.' },
      { name: 'Rosette Expansion', dayRangeMin: 8, dayRangeMax: 20, heightInches: 5, description: 'Leaves arrange in a circular overlapping ring. Water uptake rises moderately.', careTips: 'Ensure no weeds choke the perimeter. Maintain stable sub-irrigation.' },
      { name: 'Hearting & Heading', dayRangeMin: 21, dayRangeMax: 40, heightInches: 9, description: 'Central leaves gather tightly to form the upright Romaine hearts.', careTips: 'Keep reservoir topped off. Avoid dry spells which induce bitter taste.' },
      { name: 'Crisp Maturity', dayRangeMin: 41, dayRangeMax: 60, heightInches: 12, description: 'Heads are dense, firm, and fully grown. Ready for crisp morning harvesting.', careTips: 'Cut the entire head at the base, or harvest outer leaves for continuous cut-and-come-again yields.' }
    ],
    earthboxLayout: {
      totalPlants: 6,
      fertilizerBand: 'center',
      diagramPoints: [
        { x: 20, y: 25 }, { x: 50, y: 25 }, { x: 80, y: 25 },
        { x: 20, y: 75 }, { x: 50, y: 75 }, { x: 80, y: 75 }
      ],
      instructions: 'Place exactly 6 Romaine lettuce seedlings in two parallel rows. Put the fertilizer band down the center of the box between the rows.'
    }
  },
  {
    id: 'slicing_cucumber',
    name: 'Marketmore Slicing Cucumber',
    scientificName: 'Cucumis sativus',
    expectedYield: '10-15 cucumbers per box',
    waterUsage: 'High',
    stages: [
      { name: 'Sprout', dayRangeMin: 0, dayRangeMax: 8, heightInches: 2, description: 'Stout green sprouts expand their seed leaves.', careTips: 'Keep soil moist.' },
      { name: 'Climbing Tendrils', dayRangeMin: 9, dayRangeMax: 25, heightInches: 10, description: 'Vines start searching for support rails with wiry tendrils.', careTips: 'Set up vertical netting/trellis.' },
      { name: 'Flowering Stage', dayRangeMin: 26, dayRangeMax: 45, heightInches: 24, description: 'Bright yellow male and female blossoms open. Bees carry pollen.', careTips: 'Ensure adequate outdoor ventilation for bees.' },
      { name: 'Harvest Season', dayRangeMin: 46, dayRangeMax: 80, heightInches: 48, description: 'Crisp green slicing cucumbers reach 6-8 inches.', careTips: 'Pick regularly to keep vines producing new fruit.' }
    ],
    earthboxLayout: {
      totalPlants: 4,
      fertilizerBand: 'center',
      diagramPoints: [
        { x: 20, y: 35 }, { x: 40, y: 35 }, { x: 60, y: 35 }, { x: 80, y: 35 }
      ],
      instructions: 'Plant 4 cucumber starts in a single row down the center. Place the fertilizer band on the side.'
    }
  },
  {
    id: 'sweet_basil',
    name: 'Genovese Sweet Basil',
    scientificName: 'Ocimum basilicum',
    expectedYield: 'Continuous leafy yields',
    waterUsage: 'Medium',
    stages: [
      { name: 'Seedling', dayRangeMin: 0, dayRangeMax: 9, heightInches: 2, description: 'Highly aromatic tiny seedlings sprout.', careTips: 'Pinch top leaves if they grow too tall.' },
      { name: 'Branching Bush', dayRangeMin: 10, dayRangeMax: 30, heightInches: 8, description: 'Side branches multiply rapidly upon pruning.', careTips: 'Snip the central grow tip above node 3.' },
      { name: 'Lush Foliage', dayRangeMin: 31, dayRangeMax: 90, heightInches: 18, description: 'Incredible green leaves ideal for fresh pesto.', careTips: 'Pinch off any emerging white flower buds to maintain oil content.' }
    ],
    earthboxLayout: {
      totalPlants: 6,
      fertilizerBand: 'double-side',
      diagramPoints: [
        { x: 25, y: 30 }, { x: 50, y: 30 }, { x: 75, y: 30 },
        { x: 25, y: 70 }, { x: 50, y: 70 }, { x: 75, y: 70 }
      ],
      instructions: 'Plant 6 basil plugs. Use two fertilizer bands near both outer margins.'
    }
  }
];

export function getPlantById(id: string): PlantType | undefined {
  return EXHAUSTIVE_PLANT_CATALOG.find(p => p.id === id);
}
