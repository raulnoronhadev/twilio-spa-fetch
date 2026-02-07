import { CssBaseline, ThemeProvider } from '@mui/material';
import { Routes, Route } from "react-router-dom";
import Login from './pages/Login/Login';

export default function App() {

  return (
    <>
      <CssBaseline />
      <div className="app">
        <main className="content">
          <Routes>
            <Route path="/" element={<Login />} />
          </Routes>
        </main>
      </div>
    </>
  )
}