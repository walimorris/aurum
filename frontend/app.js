import React from 'react';
import ReactDOM from 'react-dom';
import {BrowserRouter, Route, Routes} from "react-router-dom";

import Container from './js/Container';


const App = () => {

    return(
        <div id={'app'}>
            <BrowserRouter>
                <Routes>
                    <Route path='/' element={<Container />} />
                </Routes>
            </BrowserRouter>
        </div>
    )
};
ReactDOM.render(
    <App />,
    document.getElementById('root')
);