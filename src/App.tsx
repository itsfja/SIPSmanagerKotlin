import React, { useState, useEffect } from 'react';
import {
  Sun,
  Cloud,
  Droplet,
  Snowflake,
  Wind,
  Plus,
  Trash2,
  LogOut,
  ChevronDown,
  ChevronUp,
  Search,
  BookOpen,
  Info,
  CheckCircle,
  Clock,
  AlertTriangle,
  Flame,
  Download
} from 'lucide-react';
import { EXHAUSTIVE_PLANT_CATALOG, getPlantById } from './data/plants';
import { EXHAUSTIVE_PEST_DISEASE_CATALOG } from './data/pests';
import { geocodePostcode, fetchLocalWeather, getFallbackWeather, estimateLastFrostDate } from './utils/weather';
import { UserProfile, PlanterBox, NotificationAlert, WeatherData, PlantType } from './types';

// Simple Local Storage Helpers
function getStoredProfiles(): UserProfile[] {
  const data = localStorage.getItem('sips_profiles');
  return data ? JSON.parse(data) : [];
}

function saveStoredProfiles(profiles: UserProfile[]) {
  localStorage.setItem('sips_profiles', JSON.stringify(profiles));
}

function getStoredActiveProfile(): UserProfile | null {
  const data = localStorage.getItem('sips_active_profile');
  return data ? JSON.parse(data) : null;
}

function saveStoredActiveProfile(profile: UserProfile | null) {
  if (profile) {
    localStorage.setItem('sips_active_profile', JSON.stringify(profile));
  } else {
    localStorage.removeItem('sips_active_profile');
  }
}

function getStoredPlanters(username: string): PlanterBox[] {
  const data = localStorage.getItem(`sips_planters_${username}`);
  if (data) return JSON.parse(data);
  
  // Seed initial values
  const defaultPlanters: PlanterBox[] = [
    {
      id: 'tomato_box_1',
      name: 'Patio Earthbox 1',
      createdAt: Date.now() - 25 * 24 * 60 * 60 * 1000,
      plantId: 'cherry_tomato',
      plantCount: 2,
      plantingDate: Date.now() - 25 * 24 * 60 * 60 * 1000,
      waterReservoirLevel: 45,
      soilMoisture: 65,
      notes: [
        'Planted cherry tomato seedlings with standard cage support.',
        'Added 2 cups of dolomite.',
        'Sprouted nicely on day 7!'
      ],
      planterType: 'earthbox'
    },
    {
      id: 'basil_box_1',
      name: 'Windowsill Box',
      createdAt: Date.now() - 5 * 24 * 60 * 60 * 1000,
      plantId: 'sweet_basil',
      plantCount: 6,
      plantingDate: Date.now() - 5 * 24 * 60 * 60 * 1000,
      waterReservoirLevel: 80,
      soilMoisture: 75,
      notes: [
        'Seated 6 sweet basil starts.',
        'Placed central fertilizer band.'
      ],
      planterType: 'ourwarm_windowsill'
    }
  ];
  localStorage.setItem(`sips_planters_${username}`, JSON.stringify(defaultPlanters));
  return defaultPlanters;
}

function saveStoredPlanters(username: string, planters: PlanterBox[]) {
  localStorage.setItem(`sips_planters_${username}`, JSON.stringify(planters));
}

function getStoredAlerts(username: string): NotificationAlert[] {
  const data = localStorage.getItem(`sips_alerts_${username}`);
  return data ? JSON.parse(data) : [];
}

function saveStoredAlerts(username: string, alerts: NotificationAlert[]) {
  localStorage.setItem(`sips_alerts_${username}`, JSON.stringify(alerts));
}

function downloadProjectZip(e?: React.MouseEvent) {
  if (e) e.preventDefault();
  fetch('/api/download-zip')
    .then(res => {
      if (!res.ok) throw new Error('Network response failed');
      return res.blob();
    })
    .then(blob => {
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'sips-manager-source.zip';
      document.body.appendChild(a);
      a.click();
      a.remove();
      window.URL.revokeObjectURL(url);
    })
    .catch(() => {
      window.location.href = '/api/download-zip';
    });
}

function DownloadButton({ variant = 'default' }: { variant?: 'default' | 'compact' }) {
  if (variant === 'compact') {
    return (
      <button
        onClick={downloadProjectZip}
        title="Download Complete Source Code (.ZIP)"
        className="inline-flex items-center gap-1.5 px-3 py-1.5 bg-[#FEF3C7] hover:bg-[#FDE68A] text-[#78350F] font-sans font-semibold rounded-xl text-xs transition-colors border border-[#F59E0B] shadow-xs cursor-pointer"
      >
        <Download className="h-3.5 w-3.5 text-[#B45309]" />
        <span>Export .ZIP</span>
      </button>
    );
  }

  return (
    <button
      onClick={downloadProjectZip}
      className="inline-flex items-center gap-2 px-4 py-2 bg-[#FEF3C7] hover:bg-[#FDE68A] text-[#78350F] font-sans font-semibold rounded-xl text-xs transition-colors border border-[#F59E0B] shadow-xs cursor-pointer"
    >
      <Download className="h-4 w-4 text-[#B45309]" />
      <span>Download Complete Project Source (.ZIP)</span>
    </button>
  );
}

export default function App() {
  const [activeUser, setActiveUser] = useState<UserProfile | null>(getStoredActiveProfile());
  const [profiles, setProfiles] = useState<UserProfile[]>(getStoredProfiles());

  const handleLogin = (user: UserProfile) => {
    saveStoredActiveProfile(user);
    setActiveUser(user);

    // Save to list
    const updated = [...profiles];
    const existsIdx = updated.findIndex(p => p.username.toLowerCase() === user.username.toLowerCase());
    if (existsIdx !== -1) {
      updated[existsIdx] = user;
    } else {
      updated.push(user);
    }
    setProfiles(updated);
    saveStoredProfiles(updated);
  };

  const handleLogout = () => {
    saveStoredActiveProfile(null);
    setActiveUser(null);
  };

  const handleRemoveProfile = (username: string, e: React.MouseEvent) => {
    e.stopPropagation();
    const updated = profiles.filter(p => p.username !== username);
    setProfiles(updated);
    saveStoredProfiles(updated);

    localStorage.removeItem(`sips_planters_${username}`);
    localStorage.removeItem(`sips_alerts_${username}`);

    if (activeUser?.username === username) {
      setActiveUser(null);
      saveStoredActiveProfile(null);
    }
  };

  return (
    <div className="min-h-screen bg-[#FCF9F2] text-[#27272A] font-sans selection:bg-[#FEF3C7] selection:text-[#78350F]">
      {!activeUser ? (
        <AuthScreen
          profiles={profiles}
          onLogin={handleLogin}
          onRemoveProfile={handleRemoveProfile}
        />
      ) : (
        <Dashboard
          user={activeUser}
          onLogout={handleLogout}
          prefsManager={{
            getPlanters: (username: string) => getStoredPlanters(username),
            savePlanters: (username: string, list: PlanterBox[]) => saveStoredPlanters(username, list),
            getAlerts: (username: string) => getStoredAlerts(username),
            saveAlerts: (username: string, list: NotificationAlert[]) => saveStoredAlerts(username, list)
          }}
        />
      )}

      {/* Humble credit and internal version alignment */}
      <footer className="text-center py-8 text-xs text-[#9A7D6F] font-mono space-y-3">
        <p>SIPS Manager App • Version 2.7 • Traditional British Cottage Craft</p>
        <div>
          <DownloadButton />
        </div>
      </footer>
    </div>
  );
}

/* ==========================================
   AUTH SCREEN COMPONENT
   ========================================== */
interface AuthScreenProps {
  profiles: UserProfile[];
  onLogin: (user: UserProfile) => void;
  onRemoveProfile: (username: string, e: React.MouseEvent) => void;
}

function AuthScreen({ profiles, onLogin, onRemoveProfile }: AuthScreenProps) {
  const [username, setUsername] = useState('');
  const [postcode, setPostcode] = useState('');
  const [isLoading, setIsLoading] = useState(false);
  const [errorMsg, setErrorMsg] = useState<string | null>(null);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!username.trim() || !postcode.trim()) {
      setErrorMsg('Please supply both grower name and location postcode.');
      return;
    }

    setIsLoading(true);
    setErrorMsg(null);

    try {
      const geocode = await geocodePostcode(postcode);
      const frost = estimateLastFrostDate(geocode.latitude);

      const profile: UserProfile = {
        username: username.trim(),
        postcode: postcode.trim().toUpperCase(),
        city: geocode.name,
        state: geocode.admin1,
        country: geocode.country,
        lastFrostDate: frost,
        avatarSeed: username.trim().slice(0, 2).toUpperCase()
      };

      onLogin(profile);
    } catch (err) {
      setErrorMsg('Lookup failed. Please verify your postcode.');
    } finally {
      setIsLoading(false);
    }
  };

  return (
    <div className="max-w-md mx-auto px-4 py-16 flex flex-col items-center justify-center min-h-[85vh]">
      {/* Title Card */}
      <div className="w-full bg-white rounded-2xl border-2 border-[#92400E] shadow-sm p-6 text-center mb-8 relative">
        <div className="inline-flex items-center justify-center p-3 bg-[#FEF3C7] rounded-full mb-3">
          <Flame className="h-8 w-8 text-[#B45309]" />
        </div>
        <h1 className="text-3xl font-serif font-bold text-[#78350F] tracking-tight">SIPS Manager</h1>
        <p className="text-xs text-[#B45309] italic mt-1 mb-4">Peter & Jane's Digital Garden Companion</p>
        <div className="pt-2 border-t border-[#FEE2E2]">
          <DownloadButton variant="compact" />
        </div>
      </div>

      {/* Form Card */}
      <div className="w-full bg-white rounded-2xl border border-[#E4E4E7] shadow-sm p-6 mb-8">
        <h2 className="text-md font-bold text-[#27272A] mb-4">Step into the Garden</h2>
        <form onSubmit={handleSubmit} className="space-y-4">
          <div>
            <label className="block text-xs font-medium text-[#71717A] mb-1">Your Name (Grower)</label>
            <input
              type="text"
              value={username}
              onChange={e => setUsername(e.target.value)}
              placeholder="e.g. Peter, Jane, Mummy"
              className="w-full px-3 py-2 border border-[#E4E4E7] rounded-lg text-sm focus:outline-none focus:border-[#B45309]"
            />
          </div>

          <div>
            <label className="block text-xs font-medium text-[#71717A] mb-1">Your Postcode / Zip</label>
            <input
              type="text"
              value={postcode}
              onChange={e => setPostcode(e.target.value)}
              placeholder="e.g. WA11 0SD or 90210"
              className="w-full px-3 py-2 border border-[#E4E4E7] rounded-lg text-sm focus:outline-none focus:border-[#B45309]"
            />
          </div>

          {errorMsg && <p className="text-xs text-[#EF4444]">{errorMsg}</p>}

          <button
            type="submit"
            disabled={isLoading}
            className="w-full py-2 bg-[#B45309] hover:bg-[#92400E] text-white font-bold rounded-lg text-sm transition-colors flex items-center justify-center gap-2"
          >
            {isLoading ? 'Verifying...' : 'Proceed to Garden Dashboard'}
          </button>
        </form>
      </div>

      {/* Stored Users List */}
      {profiles.length > 0 && (
        <div className="w-full bg-white rounded-2xl border border-[#E4E4E7] shadow-sm p-6">
          <h3 className="text-xs font-bold text-[#71717A] uppercase tracking-wider mb-3">
            Recent Growers on This Device
          </h3>
          <div className="space-y-2">
            {profiles.map(p => (
              <div
                key={p.username}
                onClick={() => onLogin(p)}
                className="w-full flex items-center justify-between p-3 bg-[#FAFAFA] hover:bg-[#F4F4F5] border border-[#F4F4F5] rounded-xl cursor-pointer transition-colors"
              >
                <div className="flex items-center gap-3">
                  <div className="h-9 w-9 bg-[#FEF3C7] text-[#D97706] font-bold text-xs rounded-full flex items-center justify-center">
                    {p.avatarSeed}
                  </div>
                  <div className="text-left">
                    <p className="text-sm font-semibold text-[#18181B]">{p.username}</p>
                    <p className="text-xs text-[#71717A]">{p.postcode} • {p.city || 'UK'}</p>
                  </div>
                </div>

                <button
                  onClick={e => onRemoveProfile(p.username, e)}
                  className="p-2 hover:bg-[#FEE2E2] rounded-lg text-[#EF4444] transition-colors"
                  title="Remove grower"
                >
                  <Trash2 className="h-4 w-4" />
                </button>
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}

/* ==========================================
   DASHBOARD SCREEN COMPONENT
   ========================================== */
interface DashboardProps {
  user: UserProfile;
  onLogout: () => void;
  prefsManager: {
    getPlanters: (username: string) => PlanterBox[];
    savePlanters: (username: string, list: PlanterBox[]) => void;
    getAlerts: (username: string) => NotificationAlert[];
    saveAlerts: (username: string, list: NotificationAlert[]) => void;
  };
}

function Dashboard({ user, onLogout, prefsManager }: DashboardProps) {
  const [weather, setWeather] = useState<WeatherData | null>(null);
  const [isWeatherLoading, setIsWeatherLoading] = useState(true);
  const [planters, setPlanters] = useState<PlanterBox[]>([]);
  const [alerts, setAlerts] = useState<NotificationAlert[]>([]);
  const [isAlertsOpen, setIsAlertsOpen] = useState(false);
  const [isAddOpen, setIsAddOpen] = useState(false);
  const [expandedPlanterId, setExpandedPlanterId] = useState<string | null>(null);

  // Add Planter states
  const [newLabel, setNewLabel] = useState('');
  const [newPlantId, setNewPlantId] = useState(EXHAUSTIVE_PLANT_CATALOG[0].id);
  const [newVesselType, setNewVesselType] = useState('earthbox');

  useEffect(() => {
    // Load local SIPS data
    setPlanters(prefsManager.getPlanters(user.username));
    setAlerts(prefsManager.getAlerts(user.username));

    // Load active weather
    const loadWeather = async () => {
      setIsWeatherLoading(true);
      try {
        const geocode = await geocodePostcode(user.postcode);
        const res = await fetchLocalWeather(geocode.latitude, geocode.longitude);
        setWeather(res);

        // Populate unread warnings
        const savedAlerts = prefsManager.getAlerts(user.username);
        const merged = [...savedAlerts];
        res.alerts.forEach(newAlert => {
          if (!merged.some(a => a.id === newAlert.id)) {
            merged.unshift(newAlert);
          }
        });
        setAlerts(merged);
        prefsManager.saveAlerts(user.username, merged);
      } catch (e) {
        setWeather(getFallbackWeather());
      } finally {
        setIsWeatherLoading(false);
      }
    };

    loadWeather();
  }, [user.postcode, user.username]);

  const handleMarkAllAlertsRead = () => {
    const updated = alerts.map(a => ({ ...a, read: true }));
    setAlerts(updated);
    prefsManager.saveAlerts(user.username, updated);
  };

  const handleAddPlanter = (e: React.FormEvent) => {
    e.preventDefault();
    if (!newLabel.trim()) return;

    const selectedPlant = getPlantById(newPlantId);
    if (!selectedPlant) return;

    const newBox: PlanterBox = {
      id: Math.random().toString(36).substr(2, 9),
      name: newLabel.trim(),
      createdAt: Date.now(),
      plantId: newPlantId,
      plantCount: selectedPlant.earthboxLayout.totalPlants,
      plantingDate: Date.now(),
      waterReservoirLevel: 100,
      soilMoisture: 85,
      notes: [`Successfully planted ${selectedPlant.name} seedlings in ${newLabel.trim()}!`],
      planterType: newVesselType
    };

    const updated = [...planters, newBox];
    setPlanters(updated);
    prefsManager.savePlanters(user.username, updated);

    // Reset Form
    setNewLabel('');
    setIsAddOpen(false);
  };

  const handleWaterPlanter = (id: string) => {
    const updated = planters.map(p => {
      if (p.id === id) {
        return {
          ...p,
          waterReservoirLevel: 100,
          soilMoisture: 90,
          refilledAt: Date.now(),
          notes: ['Reservoir topped off fully.', ...p.notes]
        };
      }
      return p;
    });
    setPlanters(updated);
    prefsManager.savePlanters(user.username, updated);
  };

  const handleSimulateTime = (id: string) => {
    const updated = planters.map(p => {
      if (p.id === id) {
        const remainingWater = Math.max(0, p.waterReservoirLevel - 20);
        const remainingSoil = Math.max(20, p.soilMoisture - 15);
        return {
          ...p,
          plantingDate: p.plantingDate - 10 * 24 * 60 * 60 * 1000, // 10 days ago jump
          waterReservoirLevel: remainingWater,
          soilMoisture: remainingSoil,
          notes: ['Simulated 10 days of fast growing weather.', ...p.notes]
        };
      }
      return p;
    });
    setPlanters(updated);
    prefsManager.savePlanters(user.username, updated);
  };

  const handleAddDiaryNote = (id: string, text: string) => {
    const updated = planters.map(p => {
      if (p.id === id) {
        return {
          ...p,
          notes: [text, ...p.notes]
        };
      }
      return p;
    });
    setPlanters(updated);
    prefsManager.savePlanters(user.username, updated);
  };

  const handleDeletePlanter = (id: string) => {
    const updated = planters.filter(p => p.id !== id);
    setPlanters(updated);
    prefsManager.savePlanters(user.username, updated);
  };

  const unreadAlertsCount = alerts.filter(a => !a.read).length;

  return (
    <div className="max-w-2xl mx-auto px-4 py-6 space-y-6">
      {/* Top Banner Coordination bar */}
      <div className="flex items-center justify-between bg-white rounded-2xl border border-[#E4E4E7] p-4 shadow-sm">
        <div className="flex items-center gap-3">
          <div className="h-10 w-10 bg-[#FEF3C7] text-[#D97706] font-bold text-sm rounded-full flex items-center justify-center">
            {user.avatarSeed}
          </div>
          <div>
            <h2 className="text-md font-serif font-bold text-[#78350F]">{user.username}'s Garden Console</h2>
            <p className="text-xs text-[#71717A]">{user.city || 'Saint Helens'} • {user.postcode}</p>
          </div>
        </div>

        <div className="flex items-center gap-2">
          {/* Export ZIP Button */}
          <DownloadButton variant="compact" />

          {/* Alerts center button */}
          <button
            onClick={() => setIsAlertsOpen(!isAlertsOpen)}
            className="relative p-2 hover:bg-[#F4F4F5] rounded-xl transition-colors"
          >
            <NotificationsBadge count={unreadAlertsCount} />
          </button>

          {/* Logout button */}
          <button
            onClick={onLogout}
            className="p-2 hover:bg-[#FEE2E2] text-[#EF4444] rounded-xl transition-colors"
            title="Log out"
          >
            <LogOut className="h-5 w-5" />
          </button>
        </div>
      </div>

      {/* System Alerts Dropdown Drawer */}
      {isAlertsOpen && (
        <div className="bg-[#FEF2F2] border border-[#FCA5A5] rounded-2xl p-4 space-y-3">
          <div className="flex items-center justify-between">
            <h3 className="text-sm font-bold text-[#991B1B]">Active SIPS System Alerts</h3>
            <button onClick={handleMarkAllAlertsRead} className="text-xs text-[#DC2626] hover:underline font-semibold">
              Mark all read
            </button>
          </div>

          {alerts.length === 0 ? (
            <p className="text-xs text-[#71717A]">No alerts detected.</p>
          ) : (
            <div className="space-y-2">
              {alerts.map(a => (
                <div key={a.id} className={`flex gap-3 p-3 rounded-lg ${a.read ? 'bg-[#FEE2E2]/50' : 'bg-[#FEE2E2]'}`}>
                  <div className="mt-0.5 text-[#991B1B]">
                    {a.type === 'freeze' ? <Snowflake className="h-4 w-4" /> : a.type === 'storm' ? <Wind className="h-4 w-4" /> : <Info className="h-4 w-4" />}
                  </div>
                  <div>
                    <h4 className="text-xs font-bold text-[#7F1D1D]">{a.title}</h4>
                    <p className="text-xs text-[#991B1B] leading-relaxed mt-0.5">{a.message}</p>
                  </div>
                </div>
              ))}
            </div>
          )}
        </div>
      )}

      {/* Weather Info Widget */}
      {isWeatherLoading ? (
        <div className="bg-white rounded-2xl border border-[#E4E4E7] h-32 flex items-center justify-center">
          <Clock className="h-6 w-6 text-[#B45309] animate-spin" />
        </div>
      ) : (
        weather && (
          <WeatherCardWidget
            weather={weather}
            lastFrostDate={user.lastFrostDate}
          />
        )
      )}

      {/* Planters Headline Row */}
      <div className="flex items-center justify-between">
        <h3 className="text-lg font-serif font-bold text-[#18181B]">My Tended Planters ({planters.length})</h3>
        <button
          onClick={() => setIsAddOpen(!isAddOpen)}
          className="px-4 py-2 bg-[#16A34A] hover:bg-[#15803D] text-white font-bold rounded-lg text-xs transition-colors flex items-center gap-1.5"
        >
          <Plus className="h-3.5 w-3.5" />
          Add Planter
        </button>
      </div>

      {/* Add Planter Form Panel */}
      {isAddOpen && (
        <form onSubmit={handleAddPlanter} className="bg-white rounded-2xl border border-[#E4E4E7] p-5 space-y-4 shadow-sm">
          <h4 className="text-sm font-bold text-[#18181B]">Add New Planter Box / Grow Bag</h4>
          
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div>
              <label className="block text-xs font-medium text-[#71717A] mb-1">Planter Label / Name</label>
              <input
                type="text"
                value={newLabel}
                onChange={e => setNewLabel(e.target.value)}
                placeholder="e.g. Patio Earthbox A"
                className="w-full px-3 py-2 border border-[#E4E4E7] rounded-lg text-sm focus:outline-none focus:border-[#16A34A]"
              />
            </div>

            <div>
              <label className="block text-xs font-medium text-[#71717A] mb-1">Choose Crop Variety</label>
              <select
                value={newPlantId}
                onChange={e => setNewPlantId(e.target.value)}
                className="w-full px-3 py-2 border border-[#E4E4E7] rounded-lg text-sm bg-white focus:outline-none focus:border-[#16A34A]"
              >
                {EXHAUSTIVE_PLANT_CATALOG.map(p => (
                  <option key={p.id} value={p.id}>{p.name}</option>
                ))}
              </select>
            </div>
          </div>

          <div>
            <label className="block text-xs font-medium text-[#71717A] mb-1">Choose Box / Containment Vessel</label>
            <select
              value={newVesselType}
              onChange={e => setNewVesselType(e.target.value)}
              className="w-full px-3 py-2 border border-[#E4E4E7] rounded-lg text-sm bg-white focus:outline-none focus:border-[#16A34A]"
            >
              <option value="earthbox">Earthbox Standard</option>
              <option value="hozelock_growbag">Hozelock Growbag</option>
              <option value="emsco_citypicker">Emsco Citypicker</option>
              <option value="ourwarm_windowsill">OurWarm Windowsill</option>
              <option value="earthbox_junior">Earthbox Junior</option>
            </select>
          </div>

          <button
            type="submit"
            className="w-full py-2 bg-[#16A34A] hover:bg-[#15803D] text-white font-bold rounded-lg text-sm transition-colors"
          >
            Plant & Register SIPS Box
          </button>
        </form>
      )}

      {/* Planter boxes listing */}
      <div className="space-y-4">
        {planters.map(planter => {
          const plant = getPlantById(planter.plantId);
          if (!plant) return null;

          return (
            <PlanterBoxCardItem
              key={planter.id}
              planter={planter}
              plant={plant}
              isExpanded={expandedPlanterId === planter.id}
              onToggle={() => setExpandedPlanterId(expandedPlanterId === planter.id ? null : planter.id)}
              onWater={() => handleWaterPlanter(planter.id)}
              onSimulate={() => handleSimulateTime(planter.id)}
              onAddNote={(text) => handleAddDiaryNote(planter.id, text)}
              onDelete={() => handleDeletePlanter(planter.id)}
            />
          );
        })}
      </div>

      {/* Diagnostics assistant module */}
      <PestDiagnosticWidget />
    </div>
  );
}

/* ==========================================
   WEATHER CARD WIDGET
   ========================================== */
interface WeatherCardWidgetProps {
  weather: WeatherData;
  lastFrostDate?: string;
}

function WeatherCardWidget({ weather, lastFrostDate }: WeatherCardWidgetProps) {
  const isFrostRisk = lastFrostDate?.includes('Frosts predicted') === true;

  return (
    <div className="bg-white rounded-2xl border border-[#E4E4E7] p-5 shadow-sm space-y-4">
      <div className="flex items-center justify-between">
        <div>
          <p className="text-[10px] font-bold text-[#71717A] uppercase tracking-wider">Local Weather Forecast</p>
          <div className="flex items-center gap-3 mt-1">
            <h4 className="text-3xl font-bold text-[#18181B]">{weather.temp}°F</h4>
            <div className="text-[#B45309]">
              {weather.state === 'Sunny' ? <Sun className="h-7 w-7 text-amber-500 animate-spin-slow" /> : weather.state === 'Cloudy' ? <Cloud className="h-7 w-7 text-slate-400" /> : <Droplet className="h-7 w-7 text-blue-500" />}
            </div>
          </div>
        </div>

        <div className="text-right text-xs text-[#71717A] space-y-1">
          <p className="font-semibold text-[#18181B]">{weather.state}</p>
          <p>Humidity: {weather.humidity}%</p>
          <p>Wind speed: {weather.windSpeed} km/h</p>
        </div>
      </div>

      {/* Frost risk strip alignment */}
      {lastFrostDate && (
        <div className={`p-3 rounded-lg border flex gap-2.5 ${isFrostRisk ? 'bg-red-50 border-red-200 text-red-900' : 'bg-green-50 border-green-200 text-green-900'}`}>
          <div className="mt-0.5">
            {isFrostRisk ? <AlertTriangle className="h-4 w-4 text-red-600" /> : <CheckCircle className="h-4 w-4 text-green-600" />}
          </div>
          <div>
            <p className="text-xs font-bold leading-none">{lastFrostDate}</p>
            {isFrostRisk && (
              <p className="text-[10px] text-red-700 leading-relaxed mt-1">
                Soil sub-surface models verify that seedlings shouldn't be planted out of cold frames until soil matches 60°F consistently.
              </p>
            )}
          </div>
        </div>
      )}

      {/* Forecast carousel row */}
      <div className="grid grid-cols-4 gap-2 pt-2">
        {weather.forecast.map((f, i) => (
          <div key={i} className="bg-[#FAFAFA] border border-[#F4F4F5] rounded-xl p-2.5 text-center space-y-1">
            <p className="text-[10px] font-bold text-[#71717A] uppercase">{f.day}</p>
            <div className="flex justify-center text-[#B45309] py-0.5">
              {f.state === 'Sunny' ? <Sun className="h-4 w-4 text-amber-500" /> : f.state === 'Cloudy' ? <Cloud className="h-4 w-4 text-slate-400" /> : <Droplet className="h-4 w-4 text-blue-400" />}
            </div>
            <p className="text-xs font-bold text-[#18181B]">{f.temp}°F</p>
          </div>
        ))}
      </div>
    </div>
  );
}

/* ==========================================
   PLANTER CARD ITEM COMPONENT
   ========================================== */
interface PlanterBoxCardItemProps {
  planter: PlanterBox;
  plant: PlantType;
  isExpanded: boolean;
  onToggle: () => void;
  onWater: () => void;
  onSimulate: () => void;
  onAddNote: (text: string) => void;
  onDelete: () => void;
}

function PlanterBoxCardItem({
  planter,
  plant,
  isExpanded,
  onToggle,
  onWater,
  onSimulate,
  onAddNote,
  onDelete
}: PlanterBoxCardItemProps) {
  const [noteInput, setNoteInput] = useState('');

  const daysPlanted = Math.max(0, Math.floor((Date.now() - planter.plantingDate) / (24 * 60 * 60 * 1000)));

  // Determine active growth stage
  const currentStage = plant.stages.find(s => daysPlanted >= s.dayRangeMin && daysPlanted <= s.dayRangeMax) || plant.stages[plant.stages.length - 1];

  return (
    <div className="bg-white rounded-2xl border border-[#E4E4E7] shadow-sm overflow-hidden">
      {/* Clickable Header panel */}
      <div onClick={onToggle} className="p-5 cursor-pointer hover:bg-[#FAFAFA] transition-colors space-y-3">
        <div className="flex justify-between items-start">
          <div>
            <h4 className="text-md font-serif font-bold text-[#18181B]">{planter.name}</h4>
            <p className="text-xs text-[#71717A] italic">{plant.name} ({plant.scientificName})</p>
            <p className="text-[10px] font-semibold text-[#16A34A] uppercase tracking-wider mt-1">
              Vessel: {planter.planterType.replace('_', ' ')} • Age: {daysPlanted} days
            </p>
          </div>
          <div>
            {isExpanded ? <ChevronUp className="h-5 w-5 text-[#71717A]" /> : <ChevronDown className="h-5 w-5 text-[#71717A]" />}
          </div>
        </div>

        {/* Reservoir levels layout */}
        <div className="space-y-1.5">
          <div className="flex justify-between items-center text-xs">
            <span className="font-medium text-[#71717A] flex items-center gap-1">
              <Droplet className="h-3.5 w-3.5 text-[#0284C7]" /> Sub-Irrigated Reservoir Level
            </span>
            <span className="font-bold text-[#0284C7]">{planter.waterReservoirLevel}%</span>
          </div>

          <div className="h-2 w-full bg-[#E0F2FE] rounded-full overflow-hidden">
            <div
              className="h-full bg-[#0284C7] rounded-full transition-all duration-500"
              style={{ width: `${planter.waterReservoirLevel}%` }}
            />
          </div>

          {planter.waterReservoirLevel < 30 && (
            <p className="text-[10px] text-[#EF4444] font-semibold">⚠️ Low Water! Refill the SIPS tube immediately.</p>
          )}
        </div>

        <div className="flex justify-between items-center text-[11px] text-[#71717A] pt-1">
          <span>Capillary Soil Moisture (Around Roots):</span>
          <span className="font-bold text-[#15803D]">{planter.soilMoisture}% (Optimal Wet)</span>
        </div>
      </div>

      {/* Expanded detail screen drawers */}
      {isExpanded && (
        <div className="border-t border-[#F4F4F5] p-5 bg-[#FAFAFA] space-y-4">
          {/* Diagrams layout */}
          <SipsLayoutDiagramVisualizer plant={plant} />

          {/* Current grow timeline stage card */}
          <div className="bg-white rounded-xl border border-[#E4E4E7] p-4 space-y-1.5">
            <p className="text-[10px] font-bold text-[#71717A] uppercase tracking-wider">Active Growth Phase</p>
            <h5 className="text-sm font-bold text-[#1E293B]">{currentStage.name}</h5>
            <p className="text-xs text-[#64748B]">Expected height: ~{currentStage.heightInches} inches</p>
            <p className="text-xs text-[#334155] leading-relaxed pt-1">{currentStage.description}</p>
            <p className="text-xs italic text-[#475569] pt-1">Care guide: {currentStage.careTips}</p>
          </div>

          {/* Simulator & refill triggers */}
          <div className="grid grid-cols-2 gap-3">
            <button
              onClick={onWater}
              className="py-2.5 bg-[#0284C7] hover:bg-[#0369A1] text-white font-bold rounded-xl text-xs transition-colors flex items-center justify-center gap-1.5"
            >
              <Droplet className="h-3.5 w-3.5" />
              Water Reservoir
            </button>

            <button
              onClick={onSimulate}
              className="py-2.5 bg-[#B45309] hover:bg-[#92400E] text-white font-bold rounded-xl text-xs transition-colors flex items-center justify-center gap-1.5"
            >
              <Clock className="h-3.5 w-3.5" />
              Simulate +10 Days
            </button>
          </div>

          {/* Gardening diary log lists */}
          <div className="space-y-2">
            <h5 className="text-xs font-bold text-[#27272A]">Gardening Diary Logs</h5>
            
            <form
              onSubmit={e => {
                e.preventDefault();
                if (!noteInput.trim()) return;
                onAddNote(noteInput.trim());
                setNoteInput('');
              }}
              className="flex gap-2"
            >
              <input
                type="text"
                value={noteInput}
                onChange={e => setNoteInput(e.target.value)}
                placeholder="Log fertilizer feed, blooms, pruning..."
                className="flex-1 px-3 py-1.5 bg-white border border-[#E4E4E7] rounded-lg text-xs focus:outline-none focus:border-[#16A34A]"
              />
              <button type="submit" className="px-3 bg-[#16A34A] hover:bg-[#15803D] text-white font-bold rounded-lg text-xs transition-colors">
                Log
              </button>
            </form>

            <div className="space-y-1 max-h-32 overflow-y-auto">
              {planter.notes.map((n, i) => (
                <div key={i} className="p-2 bg-[#F4F4F5] rounded-lg text-xs text-[#27272A] flex items-start gap-1.5">
                  <span className="text-[#16A34A] font-bold">•</span>
                  <span className="leading-relaxed">{n}</span>
                </div>
              ))}
            </div>
          </div>

          {/* Deletion control */}
          <button
            onClick={onDelete}
            className="w-full py-2 border border-[#FCA5A5] hover:bg-[#FEE2E2] text-[#EF4444] rounded-lg text-xs font-bold transition-colors flex items-center justify-center gap-1"
          >
            <Trash2 className="h-3.5 w-3.5" /> Remove Planter Box
          </button>
        </div>
      )}
    </div>
  );
}

/* ==========================================
   TOP DOWN DIAGRAM VISUALIZER
   ========================================== */
function SipsLayoutDiagramVisualizer({ plant }: { plant: PlantType }) {
  const { totalPlants, fertilizerBand, diagramPoints, instructions } = plant.earthboxLayout;

  return (
    <div className="bg-[#F9F9FB] rounded-xl border border-[#E4E4E7] p-3 space-y-3 text-center">
      <h5 className="text-xs font-bold text-[#27272A]">Planter Top-Down Layout Diagram</h5>

      {/* The 2D potting visualization frame */}
      <div className="relative w-full h-40 bg-[#271B12] border-2 border-[#1E293B] rounded-lg overflow-hidden">
        {/* Water Tube (represented at Top-Left) */}
        <div className="absolute top-3 left-3 h-6 w-6 bg-[#0284C7] rounded-full border-2 border-white flex items-center justify-center shadow-md">
          <div className="h-2 w-2 bg-white rounded-full animate-ping" />
        </div>

        {/* Dynamic Fertilizer Band lines */}
        {fertilizerBand === 'center' && (
          <div className="absolute top-1/2 left-0 right-0 h-2 bg-[#D97706] -translate-y-1/2 shadow-sm" title="Center Band" />
        )}
        {fertilizerBand === 'side' && (
          <div className="absolute bottom-4 left-0 right-0 h-2 bg-[#D97706] shadow-sm" title="Side Band" />
        )}
        {fertilizerBand === 'double-side' && (
          <>
            <div className="absolute top-4 left-0 right-0 h-2 bg-[#D97706] shadow-sm" />
            <div className="absolute bottom-4 left-0 right-0 h-2 bg-[#D97706] shadow-sm" />
          </>
        )}

        {/* Dynamic green seedling points */}
        {diagramPoints.map((pt, i) => (
          <div
            key={i}
            className="absolute h-8 w-8 -ml-4 -mt-4 bg-[#22C55E]/20 rounded-full flex items-center justify-center border border-[#22C55E]/40"
            style={{ left: `${pt.x}%`, top: `${pt.y}%` }}
          >
            <div className="h-4 w-4 bg-[#22C55E] rounded-full border border-white flex items-center justify-center">
              <div className="h-1.5 w-1.5 bg-white rounded-full" />
            </div>
          </div>
        ))}
      </div>

      {/* Legend guide strips */}
      <div className="flex justify-center gap-4 text-[10px] text-[#71717A]">
        <div className="flex items-center gap-1">
          <span className="h-2.5 w-2.5 bg-[#0284C7] rounded-full" /> Water Tube
        </div>
        <div className="flex items-center gap-1">
          <span className="h-2.5 w-2.5 bg-[#D97706] rounded-sm" /> Fertilizer Band ({fertilizerBand})
        </div>
        <div className="flex items-center gap-1">
          <span className="h-2.5 w-2.5 bg-[#22C55E] rounded-full" /> Seedling ({totalPlants} starts)
        </div>
      </div>

      <p className="text-[11px] text-[#52525B] text-left leading-relaxed bg-white border border-[#F4F4F5] p-2.5 rounded-lg">
        {instructions}
      </p>
    </div>
  );
}

/* ==========================================
   PEST DIAGNOSTIC ASSISTANT WIDGET
   ========================================== */
function PestDiagnosticWidget() {
  const [search, setSearch] = useState('');
  const [selectedCrop, setSelectedCrop] = useState('All Crops');
  const [expandedId, setExpandedId] = useState<string | null>(null);

  const filterCrops = ['All Crops', 'Cherry Tomato', 'Slicing Cucumber', 'Sweet Basil'];

  const filtered = EXHAUSTIVE_PEST_DISEASE_CATALOG.filter(issue => {
    const matchesSearch =
      issue.name.toLowerCase().includes(search.toLowerCase()) ||
      issue.description.toLowerCase().includes(search.toLowerCase()) ||
      issue.symptoms.some(s => s.toLowerCase().includes(search.toLowerCase()));

    const cropMatch =
      selectedCrop === 'All Crops' ||
      (selectedCrop === 'Cherry Tomato' && issue.targetCrops.includes('cherry_tomato')) ||
      (selectedCrop === 'Slicing Cucumber' && issue.targetCrops.includes('slicing_cucumber')) ||
      (selectedCrop === 'Sweet Basil' && issue.targetCrops.includes('sweet_basil'));

    return matchesSearch && cropMatch;
  });

  return (
    <div className="bg-white rounded-2xl border border-[#E4E4E7] p-5 shadow-sm space-y-4">
      {/* Header */}
      <div className="flex gap-2 items-center">
        <BookOpen className="h-5 w-5 text-[#DC2626]" />
        <div>
          <h4 className="text-sm font-bold text-[#18181B]">Pest & Disease Diagnostic Assistant</h4>
          <p className="text-[10px] text-[#71717A]">Identify foliage symptoms, environmental stresses, and organic controls</p>
        </div>
      </div>

      {/* Input controls */}
      <div className="space-y-2">
        <div className="relative">
          <Search className="absolute left-2.5 top-2.5 h-4 w-4 text-[#71717A]" />
          <input
            type="text"
            value={search}
            onChange={e => setSearch(e.target.value)}
            placeholder="Search symptoms (e.g. spots, webbing, leaves curling)..."
            className="w-full pl-9 pr-3 py-2 border border-[#E4E4E7] rounded-lg text-xs focus:outline-none focus:border-[#DC2626]"
          />
        </div>

        {/* Filter chips scroll row */}
        <div className="flex gap-1.5 overflow-x-auto pb-1">
          {filterCrops.map(crop => (
            <button
              key={crop}
              onClick={() => setSelectedCrop(crop)}
              className={`px-3 py-1.5 rounded-full text-[10px] font-medium transition-colors whitespace-nowrap ${selectedCrop === crop ? 'bg-[#FEF2F2] border border-[#FCA5A5] text-[#991B1B]' : 'bg-[#F4F4F5] text-[#52525B]'}`}
            >
              {crop}
            </button>
          ))}
        </div>
      </div>

      {/* Issues list results */}
      <div className="space-y-2.5">
        {filtered.length === 0 ? (
          <p className="text-xs text-[#71717A] italic text-center py-4">No diagnostic profiles match current criteria.</p>
        ) : (
          filtered.map(issue => {
            const isExpanded = expandedId === issue.id;

            return (
              <div key={issue.id} className="border border-[#E4E4E7] rounded-xl overflow-hidden bg-[#FAFAFA]">
                <div
                  onClick={() => setExpandedId(isExpanded ? null : issue.id)}
                  className="p-3 cursor-pointer hover:bg-[#F4F4F5] transition-colors flex items-center justify-between"
                >
                  <div>
                    <div className="flex items-center gap-2">
                      <span className="text-xs font-bold text-[#27272A]">{issue.name}</span>
                      <span className={`px-2 py-0.5 rounded text-[9px] font-bold ${issue.severity === 'Critical' ? 'bg-red-100 text-red-800' : issue.severity === 'High' ? 'bg-orange-100 text-orange-800' : 'bg-amber-100 text-amber-800'}`}>
                        {issue.severity}
                      </span>
                    </div>
                    <p className="text-[10px] text-[#71717A] uppercase mt-0.5">{issue.category}</p>
                  </div>
                  <div>
                    {isExpanded ? <ChevronUp className="h-4 w-4 text-[#71717A]" /> : <ChevronDown className="h-4 w-4 text-[#71717A]" />}
                  </div>
                </div>

                {isExpanded && (
                  <div className="border-t border-[#E4E4E7] p-3 bg-white space-y-3 text-xs text-[#52525B]">
                    <p className="leading-relaxed">{issue.description}</p>
                    
                    <div>
                      <p className="font-bold text-[#27272A] mb-1">Core Symptoms:</p>
                      <ul className="list-disc pl-4 space-y-0.5">
                        {issue.symptoms.map((s, idx) => (
                          <li key={idx}>{s}</li>
                        ))}
                      </ul>
                    </div>

                    <div className="bg-[#F0FDF4] border border-[#DCFCE7] p-3 rounded-lg text-[#166534]">
                      <p className="font-bold mb-1">Organic & Chemical-Free Treatments:</p>
                      <ul className="list-decimal pl-4 space-y-1 font-medium">
                        {issue.organicTreatment.map((t, idx) => (
                          <li key={idx} className="leading-relaxed">{t}</li>
                        ))}
                      </ul>
                    </div>

                    <div className="bg-blue-50 border border-blue-100 p-3 rounded-lg text-blue-900">
                      <p className="font-bold mb-1">Preventative Measures:</p>
                      <ul className="list-decimal pl-4 space-y-1 font-medium text-blue-800">
                        {issue.prevention.map((p, idx) => (
                          <li key={idx} className="leading-relaxed">{p}</li>
                        ))}
                      </ul>
                    </div>
                  </div>
                )}
              </div>
            );
          })
        )}
      </div>
    </div>
  );
}

/* ==========================================
   SIMPLE NOTIFICATIONS BADGE COMPONENT
   ========================================== */
function NotificationsBadge({ count }: { count: number }) {
  return (
    <div className="h-10 w-10 flex items-center justify-center rounded-xl bg-[#FAFAFA] border border-[#E4E4E7] hover:bg-[#F4F4F5] transition-colors">
      <div className="relative">
        <Clock className="h-5 w-5 text-[#78350F]" />
        {count > 0 && (
          <span className="absolute -top-1.5 -right-1.5 h-4 w-4 bg-[#EF4444] text-white text-[9px] font-bold rounded-full flex items-center justify-center">
            {count}
          </span>
        )}
      </div>
    </div>
  );
}
