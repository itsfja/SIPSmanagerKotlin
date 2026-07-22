export interface UserProfile {
  username: string;
  postcode: string;
  city?: string;
  state?: string;
  country?: string;
  lastFrostDate?: string;
  avatarSeed: string;
  hasBiometrics?: boolean;
}

export interface DiagramPoint {
  x: number;
  y: number;
}

export interface EarthboxLayout {
  totalPlants: number;
  fertilizerBand: 'center' | 'side' | 'double-side';
  diagramPoints: DiagramPoint[];
  instructions: string;
}

export interface GrowthStage {
  name: string;
  dayRangeMin: number;
  dayRangeMax: number;
  heightInches: number;
  description: string;
  careTips: string;
}

export interface PlantType {
  id: string;
  name: string;
  scientificName: string;
  expectedYield: string;
  waterUsage: 'Low' | 'Medium' | 'High' | 'Very High';
  stages: GrowthStage[];
  earthboxLayout: EarthboxLayout;
}

export interface PlanterBox {
  id: string;
  name: string;
  createdAt: number;
  plantId: string;
  plantCount: number;
  plantingDate: number;
  waterReservoirLevel: number; // percentage 0-100
  soilMoisture: number; // percentage 0-100
  notes: string[];
  planterType: string;
  refilledAt?: number;
}

export interface ForecastDay {
  day: string;
  temp: number;
  state: 'Sunny' | 'Cloudy' | 'Rainy' | 'Freezing' | 'Stormy';
}

export interface NotificationAlert {
  id: string;
  type: 'freeze' | 'water' | 'storm' | 'sys';
  title: string;
  message: string;
  timestamp: number;
  read: boolean;
}

export interface WeatherData {
  temp: number;
  state: 'Sunny' | 'Cloudy' | 'Rainy' | 'Freezing' | 'Stormy';
  humidity: number;
  windSpeed: number;
  forecast: ForecastDay[];
  alerts: NotificationAlert[];
}
