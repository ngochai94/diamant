import React, { Component } from 'react';
import SideBar from "../container/SideBar";
import PlayArea from "./PlayArea";

class App extends Component {
  render() {
    return (
      <div className="App">
        <PlayArea/>
        <SideBar/>
      </div>
    );
  }
}

export default App;
