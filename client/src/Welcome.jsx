import './App.css';
import Jumbotron from './components/Jumbotron';
import Navbar from './components/Navbar';

function Welcome() {
  return (
    <div className="App">
      <Navbar />
      <Jumbotron />
    </div>
  );
}

export default Welcome;
