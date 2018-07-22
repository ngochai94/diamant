import React from 'react';
import logger from 'redux-logger'
import ReactDOM from 'react-dom';
import App from './component/App';
import './index.css';
import { Provider } from 'react-redux';
import { applyMiddleware, createStore } from 'redux';
import rootReducer from './reducer';
import defaultState from './SampleState';

const store = createStore(rootReducer, defaultState, applyMiddleware(logger));

ReactDOM.render(
  <Provider store={store}>
    <App/>
  </Provider>,
  document.getElementById('root')
);
