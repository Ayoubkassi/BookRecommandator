import React from 'react';

function Navbar() {
  return (
    <nav className="navbar navbar-dark bg-dark navbar-expand-lg">
      <a className="navbar-brand" href="/home">
        BooksRecommandator
      </a>
      <button
        className="navbar-toggler"
        type="button"
        data-toggle="collapse"
        data-target="#navbarNav"
        aria-controls="navbarNav"
        aria-expanded="false"
        aria-label="Toggle navigation"
      >
        <span className="navbar-toggler-icon"></span>
      </button>
      <div className="collapse navbar-collapse" id="navbarNav">
        <ul className="navbar-nav ml-auto">
          <li className="nav-item active">
            <a className="nav-link" href="/">
              Home <span className="sr-only">(current)</span>
            </a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/books">
              Books
            </a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/">
              About
            </a>
          </li>
          <li className="nav-item">
            <a className="nav-link" href="/login">
              Logout
            </a>
          </li>
        </ul>
      </div>
    </nav>
  );
}

export default Navbar;
