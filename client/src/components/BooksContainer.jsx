import React from 'react'

function BooksContainer() {
  return (
    <div>
      <h1 className='text-center mt-4'>Find best books that match ur preferences</h1>
      <form className='p-4 mt-4'>
    <div className='row mb-4'>

        <div class="form-group col-4">
            <label for="exampleInput">Book title</label>
            <input type="text" class="form-control" id="exampleInput" aria-describedby="emailHelp" placeholder="Enter book title" />
        </div>
        <div class="form-group col-4">
            <label for="exampleInput">Book type</label>
            <input type="text" class="form-control" id="exampleInput" aria-describedby="emailHelp" placeholder="Enter book type" />
        </div>

        <div class="form-group col-4">
            <label for="exampleInput">Book author</label>
            <input type="text" class="form-control" id="exampleInput" aria-describedby="emailHelp" placeholder="Enter book author" />
        </div>
    </div>
    <div className='row mt-4'>
    <div class="form-group col-4">
            <label for="exampleInput">Book rating</label>
            <input type="text" class="form-control" id="exampleInput" aria-describedby="emailHelp" placeholder="Enter book title" />
        </div>
        <div class="form-group col-4">
            <label for="exampleInput">Minimal NB Pages</label>
            <input type="text" class="form-control" id="exampleInput" aria-describedby="emailHelp" placeholder="Enter book type" />
        </div>
    </div>

    <div className='h-100 d-flex align-items-center justify-content-center mt-4'>
        <button className='btn btn-primary'>SEARCH</button>
    </div>

      </form>
    </div>
  )
}

export default BooksContainer
