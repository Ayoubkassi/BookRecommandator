import React from 'react'

function Jumbotron() {
  return (
    <div class="jumbotron" style={{ maxWidth : '1400px' , margin : '50px auto' }}>
        <h1 class="display-4">Welcome to our book recommandator</h1>
        <p class="lead">Welcome to the Book Recommender App, a Java-based application that leverages the power of ontology with Jena API to provide personalized book recommendations. Whether you're an avid reader or someone looking to discover new literary gems, this app is designed to enhance your reading experience.</p>
        <hr class="my-4" />
        <p>It uses utility classes for typography and spacing to space content out within the larger container.</p>
        <p class="lead">
            <a class="btn btn-primary btn-lg" href="/" role="button">Learn more</a>
        </p>
    </div>
  )
}

export default Jumbotron
