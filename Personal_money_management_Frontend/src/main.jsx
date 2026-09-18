import React, { useEffect, useState, useCallback } from 'react';
import { createRoot } from 'react-dom/client';
import './styles.css';

const API = '';
const storedSession = () => JSON.parse(localStorage.getItem('use-session') || 'null');

async function request(path, options = {}) {
  const session = storedSession();
  const response = await fetch(`${API}${path}`, {
    ...options,
    headers: {
      'Content-Type': 'application/json',
      ...(session?.token ? { Authorization: `Bearer ${session.token}` } : {}),
      ...(options.headers || {})
    }
  });
  const text = await response.text();
  let body = null;
  try { body = text ? JSON.parse(text) : null; } catch { body = text; }
  if (!response.ok) throw new Error(body?.message || body || `Request failed (${response.status})`);
  return body;
}

function App() {
  const [session, setSession] = useState(storedSession);
  const [authMode, setAuthMode] = useState('login');
  const [page, setPage] = useState('overview');

  if (!session) {
    return <AuthScreen mode={authMode} setMode={setAuthMode} onAuthenticated={setSession} />;
  }

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <div className="brand"><span className="brand-mark">U</span><span>USE Finance</span></div>
        <div className="profile"><div className="avatar">{session.name?.slice(0, 1).toUpperCase()}</div><div><strong>{session.name}</strong><small>Personal account</small></div></div>
        <nav>
          <button className={page === 'overview' ? 'active' : ''} onClick={() => setPage('overview')}><span>◈</span> Overview</button>
          <button className={page === 'transactions' ? 'active' : ''} onClick={() => setPage('transactions')}><span>↕</span> Transactions</button>
          <button className={page === 'budget' ? 'active' : ''} onClick={() => setPage('budget')}><span>◌</span> Budget plan</button>
        </nav>
        <button className="logout" onClick={() => { localStorage.removeItem('use-session'); setSession(null); }}>Sign out <span>↗</span></button>
      </aside>
      <main className="main-content">
        <header className="topbar"><div><p className="eyebrow">{new Date().toLocaleDateString(undefined, { weekday: 'long', month: 'long', day: 'numeric' })}</p><h1>{page === 'overview' ? 'Good to see you' : page === 'transactions' ? 'Transactions' : 'Budget plan'}</h1></div><div className="top-actions"><span className="status-dot">● Synced</span><div className="avatar small">{session.name?.slice(0, 1).toUpperCase()}</div></div></header>
        {page === 'overview' && <Dashboard onNavigate={setPage} />}
        {page === 'transactions' && <Transactions />}
        {page === 'budget' && <Budget />}
      </main>
    </div>
  );
}

function AuthScreen({ mode, setMode, onAuthenticated }) {
  const [form, setForm] = useState({ name: '', email: '', password: '' });
  const [busy, setBusy] = useState(false);
  const [error, setError] = useState('');

  const submit = async (event) => {
    event.preventDefault(); setBusy(true); setError('');
    try {
      const data = await request(`/api/auth/${mode === 'login' ? 'login' : 'register'}`, { method: 'POST', body: JSON.stringify(form) });
      localStorage.setItem('use-session', JSON.stringify(data)); 
      onAuthenticated(data);
    } catch (err) { setError(err.message); } finally { setBusy(false); }
  };

  return (
    <div className="auth-page">
      <div className="auth-art">
        <div className="brand light"><span className="brand-mark">U</span><span>USE Finance</span></div>
        <div className="art-copy"><p className="eyebrow">A clearer view of your money</p><h1>Spend with intention.<br /><em>Save with confidence.</em></h1><p>One calm workspace for the everyday decisions that shape your future.</p></div>
        <div className="art-note">“Small habits become financial freedom.”</div>
      </div>
      <div className="auth-panel">
        <div className="mobile-brand"><div className="brand"><span className="brand-mark">U</span><span>USE Finance</span></div></div>
        <div className="auth-box">
          <p className="eyebrow">Welcome back</p>
          <h2>{mode === 'login' ? 'Your money, in focus.' : 'Start your clear money habit.'}</h2>
          <p className="muted">{mode === 'login' ? 'Sign in to pick up where you left off.' : 'Create a private space for your finances.'}</p>
          <div className="tabs">
            <button className={mode === 'login' ? 'selected' : ''} onClick={() => { setMode('login'); setError(''); }}>Sign in</button>
            <button className={mode === 'register' ? 'selected' : ''} onClick={() => { setMode('register'); setError(''); }}>Create account</button>
          </div>
          <form onSubmit={submit}>
            <label>Username
              <input required value={form.name} onChange={e => setForm({ ...form, name: e.target.value })} placeholder="Your username" />
            </label>
            {mode === 'register' && (
              <label>Email
                <input required type="email" value={form.email} onChange={e => setForm({ ...form, email: e.target.value })} placeholder="you@example.com" />
              </label>
            )}
            <label>Password
              <input required minLength="6" type="password" value={form.password} onChange={e => setForm({ ...form, password: e.target.value })} placeholder="At least 6 characters" />
            </label>
            {error && <div className="error">{error}</div>}
            <button className="primary wide" disabled={busy}>{busy ? 'Working...' : mode === 'login' ? 'Enter dashboard →' : 'Create my account →'}</button>
          </form>
        </div>
      </div>
    </div>
  );
}

function Dashboard({ onNavigate }) {
  const [data, setData] = useState({ totals: { totalIncome: 0, totalExpense: 0 }, categories: [], transactions: [] });
  const [period, setPeriod] = useState({ year: '', month: '', date: '' });
  const [error, setError] = useState('');

  // Fixed: Load function wrapped in useCallback to prevent memory leaks and ensure reload on tab click
  const load = useCallback(async () => {
    setError('');
    try {
      const query = new URLSearchParams(Object.entries(period).filter(([, value]) => value));
      const suffix = query.toString() ? `?${query}` : '';
      const [totals, categories, transactions] = await Promise.all([
        request(`/totals${suffix}`),
        request(`/category-percentages${suffix}`),
        request('/filterTransactions')
      ]);
      setData({ totals: totals || { totalIncome: 0, totalExpense: 0 }, categories: categories || [], transactions: transactions || [] });
    } catch (err) {
      setError(err.message);
    }
  }, [period]);

  useEffect(() => {
    load();
  }, [load]);

  const income = Number(data.totals?.totalIncome || 0);
  const expense = Number(data.totals?.totalExpense || 0);
  const savings = income - expense;
  const max = Math.max(income, expense, Math.abs(savings), 1);

  return (
    <section>
      <div className="section-heading">
        <div><p className="eyebrow">Your financial pulse</p><h2>Overview</h2></div>
        <button className="outline" onClick={() => onNavigate('transactions')}>+ Add transaction</button>
      </div>
      {error && <div className="error banner">{error}</div>}
      <div className="filter-row">
        <span>Showing</span>
        <select value={period.month} onChange={e => setPeriod({ ...period, month: e.target.value })}>
          <option value="">All months</option>
          {Array.from({ length: 12 }, (_, i) => <option key={i} value={i + 1}>{new Date(2000, i).toLocaleString(undefined, { month: 'long' })}</option>)}
        </select>
        <select value={period.year} onChange={e => setPeriod({ ...period, year: e.target.value })}>
          <option value="">All years</option>
          <option>2026</option>
          <option>2025</option>
          <option>2024</option>
        </select>
      </div>
      <div className="metric-grid">
        <Metric label="Total income" value={income} tone="income" icon="↗" />
        <Metric label="Total expenses" value={expense} tone="expense" icon="↘" />
        <Metric label="Net savings" value={savings} tone="savings" icon="✦" />
      </div>
      <div className="dashboard-grid">
        <div className="panel chart-panel">
          <div className="panel-title"><div><span className="eyebrow">Cash flow</span><h3>Income vs expenses</h3></div><span className="chip">This period</span></div>
          <div className="bar-chart">
            <Bar label="Income" amount={income} max={max} tone="income" />
            <Bar label="Expenses" amount={expense} max={max} tone="expense" />
            <Bar label="Savings" amount={savings} max={max} tone="savings" />
          </div>
        </div>
        <div className="panel">
          <div className="panel-title"><div><span className="eyebrow">Where it goes</span><h3>Category mix</h3></div></div>
          <div className="category-list">
            {data.categories?.slice(0, 5).map((item, index) => (
              <div className="category-item" key={`${item.type}-${item.category}-${index}`}>
                <span className={`category-dot c${index}`} />
                <span>{item.category}</span>
                <strong>{Number(item.percentage || 0).toFixed(1)}%</strong>
              </div>
            ))}
            {!data.categories?.length && <p className="muted">Add a few transactions to see your patterns.</p>}
          </div>
        </div>
      </div>
      <div className="panel table-panel">
        <div className="panel-title"><div><span className="eyebrow">Recent activity</span><h3>Latest transactions</h3></div><button className="text-button" onClick={() => onNavigate('transactions')}>View all →</button></div>
        <TransactionTable transactions={data.transactions?.slice(0, 5)} />
      </div>
    </section>
  );
}

function Metric({ label, value, tone, icon }) { return <div className={`metric ${tone}`}><div className="metric-top"><span>{label}</span><b>{icon}</b></div><strong>৳{value.toLocaleString(undefined, { maximumFractionDigits: 2 })}</strong><small>{tone === 'savings' ? 'Available after spending' : 'Across your account'}</small></div>; }
function Bar({ label, amount, max, tone }) { return <div className="bar-row"><span>{label}</span><div className="bar-track"><div className={`bar-fill ${tone}`} style={{ width: `${Math.min(Math.abs(amount) / max * 100, 100)}%` }} /></div><strong>৳{Math.abs(amount).toLocaleString(undefined, { maximumFractionDigits: 0 })}</strong></div>; }
function TransactionTable({ transactions = [] }) { return <div className="table-wrap"><table><thead><tr><th>Date</th><th>Type</th><th>Category</th><th>Source</th><th className="right">Amount</th></tr></thead><tbody>{transactions.map((item, index) => <tr key={index}><td>{item.date || '—'}</td><td><span className={`type ${item.type === 'income' ? 'positive' : 'negative'}`}>{item.type}</span></td><td>{item.category || 'Uncategorized'}</td><td>{item.media || '—'}</td><td className="right amount">{item.type === 'income' ? '+' : '-'}৳{Number(item.amount || 0).toLocaleString()}</td></tr>)}{!transactions.length && <tr><td colSpan="5" className="empty">No transactions yet.</td></tr>}</tbody></table></div>; }

function Transactions() {
  const [transactions, setTransactions] = useState([]);
  const [form, setForm] = useState({ type: 'expense', amount: '', category: 'Food', media: 'Cash', date: new Date().toISOString().slice(0, 10) });
  const [message, setMessage] = useState('');

  const load = useCallback(() => {
    request('/filterTransactions').then(res => setTransactions(res || [])).catch(err => setMessage(err.message));
  }, []);

  useEffect(() => { load(); }, [load]);

  const submit = async e => {
    e.preventDefault(); setMessage('');
    try {
      const { type, ...payload } = { ...form, amount: Number(form.amount) };
      await request(`/api/${type === 'income' ? 'incomes' : 'expenses'}`, { method: 'POST', body: JSON.stringify(payload) });
      setForm({ ...form, amount: '' });
      setMessage('Transaction added.');
      load();
    } catch (err) { setMessage(err.message); }
  };

  return (
    <section>
      <div className="section-heading"><div><p className="eyebrow">Keep the details honest</p><h2>Transactions</h2></div></div>
      <div className="transaction-layout">
        <div className="panel form-panel">
          <div className="panel-title"><div><span className="eyebrow">New entry</span><h3>Record a transaction</h3></div></div>
          <form onSubmit={submit}>
            <div className="segmented">
              <button type="button" className={form.type === 'expense' ? 'selected expense-tab' : ''} onClick={() => setForm({ ...form, type: 'expense' })}>Expense</button>
              <button type="button" className={form.type === 'income' ? 'selected income-tab' : ''} onClick={() => setForm({ ...form, type: 'income' })}>Income</button>
            </div>
            <label>Amount<input required type="number" min="0.01" step="0.01" value={form.amount} onChange={e => setForm({ ...form, amount: e.target.value })} placeholder="0.00" /></label>
            <label>Category<input required value={form.category} onChange={e => setForm({ ...form, category: e.target.value })} placeholder="Food, salary, rent..." /></label>
            <label>Source / note<input value={form.media} onChange={e => setForm({ ...form, media: e.target.value })} placeholder="Cash, card, bank..." /></label>
            <label>Date<input required type="date" value={form.date} onChange={e => setForm({ ...form, date: e.target.value })} /></label>
            {message && <div className={message.includes('added') ? 'success' : 'error'}>{message}</div>}
            <button className="primary wide">Save transaction →</button>
          </form>
        </div>
        <div className="panel table-panel">
          <div className="panel-title"><div><span className="eyebrow">Your ledger</span><h3>All activity</h3></div></div>
          <TransactionTable transactions={transactions} />
        </div>
      </div>
    </section>
  );
}

function Budget() {
  const [form, setForm] = useState({ startDate: new Date().toISOString().slice(0, 10), endDate: '', amount: '' });
  const [message, setMessage] = useState('');

  const save = async e => {
    e.preventDefault();
    try {
      await request(`/budget/setbudget?start_date=${form.startDate}&end_date=${form.endDate}&amount=${form.amount}`, { method: 'POST' });
      setMessage('Your budget plan is active.');
    } catch (err) { setMessage(err.message); }
  };

  return (
    <section>
      <div className="section-heading"><div><p className="eyebrow">Give your spending a shape</p><h2>Budget plan</h2></div></div>
      <div className="budget-layout">
        <div className="budget-intro">
          <span className="sun-mark">✦</span>
          <h3>A budget is a promise<br />to your future self.</h3>
          <p>Set a time window and one spending limit. We’ll keep the plan close while you make everyday choices.</p>
        </div>
        <div className="panel form-panel">
          <span className="eyebrow">New plan</span><h3>Set your spending limit</h3>
          <form onSubmit={save}>
            <label>Start date<input required type="date" value={form.startDate} onChange={e => setForm({ ...form, startDate: e.target.value })} /></label>
            <label>End date<input required type="date" value={form.endDate} onChange={e => setForm({ ...form, endDate: e.target.value })} /></label>
            <label>Total budget<input required type="number" min="1" step="0.01" value={form.amount} onChange={e => setForm({ ...form, amount: e.target.value })} placeholder="৳ 0.00" /></label>
            {message && <div className="success">{message}</div>}
            <button className="primary wide">Activate budget →</button>
          </form>
        </div>
      </div>
    </section>
  );
}

createRoot(document.getElementById('root')).render(<App />);