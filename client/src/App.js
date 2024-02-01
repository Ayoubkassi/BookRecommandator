import './App.css';
import Jumbotron from './components/Jumbotron';
import Navbar from './components/Navbar';
import BooksContainer from './components/BooksContainer';

function App() {
  return (
    <div className="App">
      <Navbar />
      {/* <Jumbotron /> */}
      <BooksContainer />
    </div>
  );
}

export default App;
