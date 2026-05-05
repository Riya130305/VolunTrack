import React, { useEffect, useState } from 'react';
import { createRoot } from 'react-dom/client';
import { Activity, Award, ClipboardCheck, LayoutDashboard, LogOut, MapPin, UserRound } from 'lucide-react';
import './index.css';
import { api, clearSession, getSession, setSession } from './api/client';

function App() {
  const [session, setSessionState] = useState(getSession());
  const [view, setView] = useState('dashboard');

  if (!session) {
    return <AuthPage onLogin={(next) => { setSession(next); setSessionState(next); }} />;
  }

  const nav = [
    ['dashboard', LayoutDashboard, 'Dashboard'],
    ['tasks', ClipboardCheck, 'Tasks'],
    ['checkin', MapPin, 'Check-in'],
    ['profile', UserRound, 'Profile'],
    ...(session.role === 'ADMIN' ? [['admin', Activity, 'Admin']] : [])
  ];

  return (
    <div className="min-h-screen">
      <header className="border-b border-slate-200 bg-white">
        <div className="mx-auto flex max-w-6xl flex-wrap items-center justify-between gap-3 px-4 py-4">
          <div>
            <h1 className="text-2xl font-bold text-ink">VolunTrack</h1>
            <p className="text-sm text-slate-500">Smart volunteer task allocation and recognition</p>
          </div>
          <div className="flex items-center gap-3">
            <span className="rounded-md bg-teal-50 px-3 py-1 text-sm font-medium text-teal-700">{session.name}</span>
            <button className="button-secondary" onClick={() => { clearSession(); setSessionState(null); }}>
              <LogOut size={16} /> Logout
            </button>
          </div>
        </div>
      </header>

      <main className="mx-auto grid max-w-6xl gap-5 px-4 py-6 md:grid-cols-[220px_1fr]">
        <nav className="panel h-fit space-y-2">
          {nav.map(([key, Icon, label]) => (
            <button key={key} className={`button w-full justify-start ${view === key ? 'bg-ink text-white' : 'bg-white text-ink hover:bg-slate-50'}`} onClick={() => setView(key)}>
              <Icon size={17} /> {label}
            </button>
          ))}
        </nav>
        <section>
          {view === 'dashboard' && <Dashboard session={session} />}
          {view === 'tasks' && <Tasks />}
          {view === 'checkin' && <CheckIn />}
          {view === 'profile' && <Profile />}
          {view === 'admin' && <Admin />}
        </section>
      </main>
    </div>
  );
}

function AuthPage({ onLogin }) {
  const [mode, setMode] = useState('login');
  const [form, setForm] = useState({
    name: 'Asha Volunteer',
    email: 'volunteer@voluntrack.dev',
    password: 'password',
    role: 'VOLUNTEER',
    skills: 'first aid, crowd management',
    availability: 'Saturday morning'
  });
  const [error, setError] = useState('');

  async function submit(event) {
    event.preventDefault();
    setError('');
    try {
      const payload = mode === 'login'
        ? { email: form.email, password: form.password }
        : {
            ...form,
            skills: form.skills.split(',').map((item) => item.trim()).filter(Boolean),
            availability: form.availability.split(',').map((item) => item.trim()).filter(Boolean)
          };
      onLogin(await api(`/auth/${mode === 'login' ? 'login' : 'register'}`, {
        method: 'POST',
        body: JSON.stringify(payload)
      }));
    } catch (err) {
      setError(err.message);
    }
  }

  return (
    <div className="grid min-h-screen place-items-center px-4">
      <form className="panel w-full max-w-md space-y-4" onSubmit={submit}>
        <div>
          <h1 className="text-3xl font-bold text-ink">VolunTrack</h1>
          <p className="mt-1 text-sm text-slate-500">Use volunteer@voluntrack.dev, admin@voluntrack.dev, or organizer@voluntrack.dev with password.</p>
        </div>
        {mode === 'register' && <input className="input" placeholder="Name" value={form.name} onChange={(e) => setForm({ ...form, name: e.target.value })} />}
        <input className="input" placeholder="Email" value={form.email} onChange={(e) => setForm({ ...form, email: e.target.value })} />
        <input className="input" placeholder="Password" type="password" value={form.password} onChange={(e) => setForm({ ...form, password: e.target.value })} />
        {mode === 'register' && (
          <>
            <select className="input" value={form.role} onChange={(e) => setForm({ ...form, role: e.target.value })}>
              <option value="VOLUNTEER">Volunteer</option>
              <option value="ORGANIZER">Organizer</option>
              <option value="ADMIN">Admin</option>
            </select>
            <input className="input" placeholder="Skills" value={form.skills} onChange={(e) => setForm({ ...form, skills: e.target.value })} />
            <input className="input" placeholder="Availability" value={form.availability} onChange={(e) => setForm({ ...form, availability: e.target.value })} />
          </>
        )}
        {error && <p className="rounded-md bg-red-50 p-3 text-sm text-red-700">{error}</p>}
        <button className="button-primary w-full">{mode === 'login' ? 'Login' : 'Create account'}</button>
        <button type="button" className="button-secondary w-full" onClick={() => setMode(mode === 'login' ? 'register' : 'login')}>
          {mode === 'login' ? 'Create a new account' : 'Back to login'}
        </button>
      </form>
    </div>
  );
}

function Dashboard({ session }) {
  return (
    <div className="space-y-5">
      <div className="panel">
        <h2 className="text-xl font-bold">Welcome, {session.name}</h2>
        <p className="mt-2 text-slate-600">Find a task, apply, get approved, check in with QR/GPS, and earn badges after completion.</p>
      </div>
      <div className="grid gap-4 md:grid-cols-3">
        <Metric icon={<ClipboardCheck />} label="Skill Matching" value="Auto score" />
        <Metric icon={<MapPin />} label="Attendance" value="QR + GPS" />
        <Metric icon={<Award />} label="Rewards" value="Points + PDF" />
      </div>
    </div>
  );
}

function Metric({ icon, label, value }) {
  return <div className="panel flex items-center gap-4 text-ink">{React.cloneElement(icon, { size: 28 })}<div><p className="text-sm text-slate-500">{label}</p><p className="text-lg font-bold">{value}</p></div></div>;
}

function Tasks() {
  const [tasks, setTasks] = useState([]);
  const [message, setMessage] = useState('');
  useEffect(() => { api('/tasks').then(setTasks).catch((err) => setMessage(err.message)); }, []);

  async function apply(taskId) {
    try {
      const application = await api(`/tasks/${taskId}/apply`, { method: 'POST' });
      setMessage(`Applied successfully. Match score: ${application.matchScore}%`);
    } catch (err) {
      setMessage(err.message);
    }
  }

  return (
    <div className="space-y-4">
      <h2 className="text-2xl font-bold">Task List</h2>
      {message && <p className="rounded-md bg-teal-50 p-3 text-sm text-teal-800">{message}</p>}
      {tasks.map((task) => (
        <div className="panel" key={task.id}>
          <div className="flex flex-wrap items-start justify-between gap-3">
            <div>
              <h3 className="text-lg font-bold">{task.title}</h3>
              <p className="mt-1 text-sm text-slate-600">{task.description}</p>
              <p className="mt-2 text-sm text-slate-500">{task.event?.title} · {task.estimatedHours} hours · {task.status}</p>
              <div className="mt-3 flex flex-wrap gap-2">{task.requiredSkills?.map((skill) => <span className="rounded-md bg-slate-100 px-2 py-1 text-xs" key={skill}>{skill}</span>)}</div>
            </div>
            <button className="button-primary" onClick={() => apply(task.id)}>Apply</button>
          </div>
        </div>
      ))}
    </div>
  );
}

function CheckIn() {
  const [form, setForm] = useState({ taskId: '1', qrCode: 'DEMO-QR-123', latitude: '28.6139', longitude: '77.2090' });
  const [message, setMessage] = useState('');

  async function send(path) {
    try {
      const result = await api(`/attendance/${path}`, {
        method: 'POST',
        body: JSON.stringify({ taskId: Number(form.taskId), qrCode: form.qrCode, latitude: Number(form.latitude), longitude: Number(form.longitude) })
      });
      setMessage(`${path === 'check-in' ? 'Checked in' : 'Checked out'} successfully. Attendance ID: ${result.id}`);
    } catch (err) {
      setMessage(err.message);
    }
  }

  function useBrowserLocation() {
    navigator.geolocation?.getCurrentPosition((position) => {
      setForm({ ...form, latitude: String(position.coords.latitude), longitude: String(position.coords.longitude) });
    });
  }

  return (
    <div className="panel max-w-xl space-y-4">
      <h2 className="text-2xl font-bold">QR + GPS Check-in</h2>
      <input className="input" value={form.taskId} onChange={(e) => setForm({ ...form, taskId: e.target.value })} placeholder="Task ID" />
      <input className="input" value={form.qrCode} onChange={(e) => setForm({ ...form, qrCode: e.target.value })} placeholder="QR code value" />
      <div className="grid gap-3 md:grid-cols-2">
        <input className="input" value={form.latitude} onChange={(e) => setForm({ ...form, latitude: e.target.value })} placeholder="Latitude" />
        <input className="input" value={form.longitude} onChange={(e) => setForm({ ...form, longitude: e.target.value })} placeholder="Longitude" />
      </div>
      <div className="flex flex-wrap gap-2">
        <button className="button-secondary" onClick={useBrowserLocation}>Use my location</button>
        <button className="button-primary" onClick={() => send('check-in')}>Check in</button>
        <button className="button-primary bg-coral hover:bg-red-500" onClick={() => send('check-out')}>Check out</button>
      </div>
      {message && <p className="rounded-md bg-slate-100 p-3 text-sm">{message}</p>}
    </div>
  );
}

function Profile() {
  const [profile, setProfile] = useState(null);
  useEffect(() => { api('/profile/me').then(setProfile); }, []);
  if (!profile) return <div className="panel">Loading profile...</div>;
  return (
    <div className="space-y-4">
      <div className="panel">
        <h2 className="text-2xl font-bold">{profile.user.name}</h2>
        <p className="text-slate-600">{profile.user.email}</p>
        <p className="mt-3 font-semibold">{profile.user.points} points · {profile.user.totalHours} hours</p>
      </div>
      <div className="panel">
        <h3 className="font-bold">Skills</h3>
        <div className="mt-3 flex flex-wrap gap-2">{profile.profile?.skills?.map((skill) => <span className="rounded-md bg-teal-50 px-2 py-1 text-sm text-teal-800" key={skill}>{skill}</span>)}</div>
      </div>
      <div className="panel">
        <h3 className="font-bold">Badges</h3>
        <div className="mt-3 grid gap-3 md:grid-cols-3">{profile.badges.map((badge) => <div className="rounded-md border border-slate-200 p-3" key={badge.id}><Award size={18} /><p className="mt-2 font-semibold">{badge.name}</p><p className="text-sm text-slate-500">{badge.description}</p></div>)}</div>
      </div>
    </div>
  );
}

function Admin() {
  const [stats, setStats] = useState(null);
  const [applications, setApplications] = useState([]);
  const [message, setMessage] = useState('');
  useEffect(() => {
    api('/admin/stats').then(setStats).catch((err) => setMessage(err.message));
    api('/tasks/applications').then(setApplications).catch(() => {});
  }, []);

  async function approve(id) {
    await api(`/admin/applications/${id}/approve`, { method: 'POST' });
    setMessage('Application approved and task assigned.');
  }

  return (
    <div className="space-y-5">
      <h2 className="text-2xl font-bold">Admin Dashboard</h2>
      {message && <p className="rounded-md bg-teal-50 p-3 text-sm text-teal-800">{message}</p>}
      {stats && <div className="grid gap-4 md:grid-cols-5">{Object.entries(stats).map(([key, value]) => <div className="panel" key={key}><p className="text-sm capitalize text-slate-500">{key}</p><p className="text-2xl font-bold">{value}</p></div>)}</div>}
      <div className="panel">
        <h3 className="font-bold">Applications</h3>
        <div className="mt-3 space-y-3">
          {applications.map((item) => <div className="flex flex-wrap items-center justify-between gap-3 border-t border-slate-100 pt-3" key={item.id}><span>{item.volunteer.name} → {item.task.title} · {item.matchScore}% · {item.status}</span><button className="button-primary" onClick={() => approve(item.id)}>Approve</button></div>)}
        </div>
      </div>
    </div>
  );
}

createRoot(document.getElementById('root')).render(<App />);
