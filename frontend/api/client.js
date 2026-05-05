const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/api';

export function getSession() {
  const raw = localStorage.getItem('voluntrack_session');
  return raw ? JSON.parse(raw) : null;
}

export function setSession(session) {
  localStorage.setItem('voluntrack_session', JSON.stringify(session));
}

export function clearSession() {
  localStorage.removeItem('voluntrack_session');
}

export async function api(path, options = {}) {
  const session = getSession();
  const response = await fetch(`${API_URL}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(session?.token ? { Authorization: `Bearer ${session.token}` } : {}),
      ...(options.headers || {})
    }
  });
  if (!response.ok) {
    const message = await response.text();
    throw new Error(message || 'Request failed');
  }
  const contentType = response.headers.get('content-type') || '';
  return contentType.includes('application/json') ? response.json() : response;
}
