export interface DiagnosticIssue {
  id: string;
  name: string;
  category: string; // "pest" | "disease" | "environmental"
  description: string;
  symptoms: string[];
  targetCrops: string[];
  organicTreatment: string[];
  prevention: string[];
  severity: string; // "Low" | "Medium" | "High" | "Critical"
  imagePlaceholderColor: string;
}

export const EXHAUSTIVE_PEST_DISEASE_CATALOG: DiagnosticIssue[] = [
  {
    id: "blossom_end_rot",
    name: "Blossom End Rot",
    category: "environmental",
    description: "A structural physiological disorder triggered by localized calcium transport failure within fast-growing fruits. It commonly affects tomatoes, peppers, and eggplants styled in sub-irrigated systems when proper calcium buffers are missing.",
    symptoms: [
      "Dark sunken leathery patches at the bottom of the fruit",
      "Water-soaked spots on the blossom-end of maturing tomatoes or peppers",
      "Premature fruit ripening or mold on the bottom spot"
    ],
    targetCrops: ["cherry_tomato"],
    organicTreatment: [
      "Top off and stabilize the sub-irrigation reservoir daily. Uneven transpiration disrupts calcium mobility.",
      "Apply a quick-release organic soluble calcium foliar spray directly to the foliage in the early morning.",
      "Remove affected fruits immediately to stop the plant from wasting structural resources on spoiled yields."
    ],
    prevention: [
      "Always pack exactly 1 to 2 cups of hydrated lime, dolomite, or agricultural gypsum into the potting medium during your initial Earthbox setup.",
      "Never mix native clay or dense compost into the Earthbox; use a lightweight, peat-coir-perlite based potting mix."
    ],
    severity: "Medium",
    imagePlaceholderColor: "from-amber-700 to-zinc-900"
  },
  {
    id: "spider_mites",
    name: "Spider Mites Infestation",
    category: "pest",
    description: "Microscopic sap-sucking arachnids that multiply extensively in warm, dusty microclimates on patio garden corners.",
    symptoms: [
      "Fine silky webbing on the undersides of leaves and around tender stems",
      "Tiniest yellow-white speckling or stippling on leaf surfaces",
      "Leaves looking dry, dusty, bronzed, and dropping prematurely"
    ],
    targetCrops: ["cherry_tomato", "slicing_cucumber", "sweet_basil"],
    organicTreatment: [
      "Blast the undersides of leaves with a high-pressure stream of cold clean water to physically break webbing and dislodge colonies.",
      "Spray affected foliage thoroughly with organic Rosemary-based oil, Neem oil, or potassium salts of fatty acids (insecticidal soap) under dim evening lighting."
    ],
    prevention: [
      "Sprinkle or mist foliage weekly during scorching dry snaps to eliminate the hot, dry, dusty conditions spider mites thrive in.",
      "Introduce native predatory mites (Phytoseiulus persimilis) to search, patrol, and devour pest mite populations sustainably."
    ],
    severity: "High",
    imagePlaceholderColor: "from-orange-500 to-yellow-700"
  },
  {
    id: "powdery_mildew",
    name: "Powdery Mildew Fungal Infection",
    category: "disease",
    description: "A prevalent airborne fungal disease that covers plant stems and leaves in a white powdery spore layer, blocking photosynthesis and arresting growth.",
    symptoms: [
      "White, talcum powder-like spots on leaf tops and stalks",
      "Leaves turning yellow, dry, curling upwards, and displaying brittle texture",
      "Stunted growth and severely reduced vegetable yield"
    ],
    targetCrops: ["slicing_cucumber", "sweet_basil"],
    organicTreatment: [
      "Gently snip and safely bag highly infected leaves (do not toss in local compost piles).",
      "Spray leaves with a mild organic preventative mixture: 1 tablespoon potassium bicarbonate, 1 teaspoon organic liquid soap, mixed in 1 gallon water.",
      "Apply organic bio-fungicides containing Bacillus subtilis."
    ],
    prevention: [
      "Never plant Earthboxes in deep shadows. Place them in zones matching their full daylight sun parameters.",
      "Always leave exactly the recommended crop seating density. Do not crowd the box."
    ],
    severity: "Medium",
    imagePlaceholderColor: "from-slate-300 to-emerald-800"
  },
  {
    id: "aphids_colony",
    name: "Aphid Colony Outbreak",
    category: "pest",
    description: "Tiny soft-bodied pear-shaped insects that pierce tender new shoots and suck out nutritious saps.",
    symptoms: [
      "Clusters of tiny bugs crowded on fresh growth and flower buds",
      "Curled, puckered, or stunted leaves",
      "Sticky clear syrup coating (honeydew) leading to dark black sooty mold"
    ],
    targetCrops: ["sweet_basil", "cherry_tomato"],
    organicTreatment: [
      "Squish minor colonies by hand or wipe them off with a damp soapy cloth.",
      "Hose off plants vigorously. Then spray a 2% dilution of pure Castile soap or organic insecticidal soap."
    ],
    prevention: [
      "Attract friendly beneficial predators like ladybugs, hoverflies, and lacewings.",
      "Avoid overloading the Earthbox with excessive high-nitrogen liquid quick-feeders."
    ],
    severity: "Low",
    imagePlaceholderColor: "from-emerald-400 to-emerald-700"
  }
];
