import { CssBaseline } from '@mui/material';
import { BrowserRouter } from 'react-router-dom';
import AppRoutes from './routes/AppRoutes';

export default function App() {

  return (
    <>
      <CssBaseline />
      <div className="app">
        <main className="content">
          <AppRoutes />
        </main>
      </div>
    </>
  )
}