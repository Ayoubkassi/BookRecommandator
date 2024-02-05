import React  , { useState } from 'react'

function BooksContainer() {

  const [query, setQuery] = useState({
    title: '',
    type: '',
    author: '',
    rating: '',
    minPages: '',
  });

  const handleInputChange = (field, value) => {
    setQuery({ ...query, [field]: value });
  };

  const [searchResults, setSearchResults] = useState([]);



//   const handleSearch = () => {
//     // Send the request with only the title
//     fetch('http://localhost:5000/api/v1/query', {
//         method: 'POST',
//         headers: {
//             'Content-Type': 'application/json',
//         },
//         body: query.title,
//     })
//     .then(response => response.json())
//     .then(data => {
//         setSearchResults(data);
//     })
//     .catch(error => {
//         console.error('Error:', error);
//     });
// };

const handleSearch = () => {
  // Send the request with only the title
  fetch('http://localhost:5000/api/v1/query', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: query.title,
  })
    .then((response) => response.json())
    .then(async (data) => {
      // Fetch cover images from Google Books API based on the title
      const coverPromises = data.map(async (result) => {
        const googleBooksResponse = await fetch(
          `https://www.googleapis.com/books/v1/volumes?q=${encodeURIComponent(result.title)}`
        );
        const googleBooksData = await googleBooksResponse.json();

        if (googleBooksData.items && googleBooksData.items.length > 0) {
          const bookInfo = googleBooksData.items[0].volumeInfo;
          return { ...result, cover: bookInfo.imageLinks?.thumbnail };
        } else {
          return result;
        }
      });

      // Wait for all cover image promises to resolve
      const resultsWithCovers = await Promise.all(coverPromises);

      setSearchResults(resultsWithCovers);
    })
    .catch((error) => {
      console.error('Error:', error);
    });
};



  



  return (
    <div>
    <h1 className='text-center mt-4'>Find best books that match your preferences</h1>
    <form className='p-4 mt-4'>
      <div className='row mb-4'>
        <div className="form-group col-4">
          <label htmlFor="titleInput">Book title</label>
          <input
            type="text"
            className="form-control"
            id="titleInput"
            placeholder="Enter book title"
            value={query.title}
            onChange={e => handleInputChange('title', e.target.value)}
          />
        </div>
        <div className="form-group col-4">
          <label htmlFor="typeInput">Book type</label>
          <input
            type="text"
            className="form-control"
            id="typeInput"
            placeholder="Enter book type"
            value={query.type}
            onChange={e => handleInputChange('type', e.target.value)}
          />
        </div>
        <div className="form-group col-4">
          <label htmlFor="authorInput">Book author</label>
          <input
            type="text"
            className="form-control"
            id="authorInput"
            placeholder="Enter book author"
            value={query.author}
            onChange={e => handleInputChange('author', e.target.value)}
          />
        </div>
      </div>
      <div className='row mt-4'>
        <div className="form-group col-4">
          <label htmlFor="ratingInput">Book rating</label>
          <input
            type="text"
            className="form-control"
            id="ratingInput"
            placeholder="Enter book rating"
            value={query.rating}
            onChange={e => handleInputChange('rating', e.target.value)}
          />
        </div>
        <div className="form-group col-4">
          <label htmlFor="minPagesInput">Minimal NB Pages</label>
          <input
            type="text"
            className="form-control"
            id="minPagesInput"
            placeholder="Enter minimal pages"
            value={query.minPages}
            onChange={e => handleInputChange('minPages', e.target.value)}
          />
        </div>
      </div>
      <div className='h-100 d-flex align-items-center justify-content-center mt-4'>
        <button type='button' className='btn btn-primary' onClick={handleSearch}>
          SEARCH
        </button>
      </div>
    </form>
    <div className='mt-4 d-flex align-items-center justify-content-center'>
        {searchResults.length > 0 && (
          <div>
            <h2>Search Results:</h2>
            <ul>
              {searchResults.map(result => (
                <div key={result.book} className="card" style={{ width: '18rem' }}>
                  {result.cover && <img className="card-img-top" src={result.cover} alt={`${result.title} cover`} />}
                  <div className="card-body">
                    <h5 className="card-title">Title: {result.title}</h5>
                    <p className="card-text">Author: {result.author}</p>
                    <a href="#" className="btn btn-primary">Genre: {result.genre && result.genre.split('#')[1]}</a>
                  </div>
                </div>
              ))}
            </ul>
          </div>
        )}
      </div>
    </div>
  )
}

export default BooksContainer
