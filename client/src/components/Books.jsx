import React , { useState  , useEffect} from 'react'

function Books() {

    const [searchResults, setSearchResults] = useState([]);

  // Function to handle search
  const handleSearch = () => {
    fetch('http://localhost:5000/api/v1/books') // Changed the endpoint to GET all books
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
            return { ...result, cover: bookInfo.imageLinks?.thumbnail ,
              averageRating: bookInfo.averageRating,
              pageCount: bookInfo.pageCount
            };
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

  // Fetch books on component mount
  useEffect(() => {
    handleSearch();
  }, []); // Empty dependency array ensures useEffect runs only once on component mount


  return (
    <div className='container' style={{ width: '90vw' }}>
        <div className='row' style={{ width: '100%', marginRight: '-15px' }}> {/* Set width to 100% */}
            {searchResults.map((result, index) => (
            <div key={index} className="col-md-3 col-md-offset-2" style={{ boxShadow: '2px 2px 5px black', padding: '15px', marginBottom: '15px'}}> {/* Add margin-right and margin-bottom */}
                {result.cover ? (
                <img className="card-img-top" src={result.cover} alt={`${result.title} cover`} />
                ) : (
                <img className="card-img-top" src="https://demo.publishr.cloud/assets/common/images/edition_placeholder.png" alt="Default cover" />
                )}
                <div className="card-body">
                <h5 className="card-title">Title: {result.title}</h5>
                <p className="card-text">Author: {result.author && result.author.split('#')[1]}</p>
                    {  result.averageRating && <p>Rating : {result.averageRating}</p>} 
                    {  result.pageCount && <p>NB pages : {result.pageCount}</p>} 
                <a href="#" className="btn btn-primary">Genre: {result.genre && result.genre.split('#')[1]}</a>
                </div>
            </div>
            ))}
        </div>
        </div>

  )
}

export default Books
