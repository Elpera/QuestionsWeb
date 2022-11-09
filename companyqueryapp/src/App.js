
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import './App.css';
import Login from './components/Login';
import Quiz from './components/Quiz';
import Result from './components/Result';

function App() {
  return (
  <BrowserRouter>
  <Routes>
    <Route path="/" element={<Login />}></Route>
    <Route path="/quiz" element={<Quiz />}></Route>
    <Route path="/result" element={<Result />}></Route>
  </Routes>
  </BrowserRouter>
  );
}
export default App;
