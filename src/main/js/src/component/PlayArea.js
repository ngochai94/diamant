import React, { Component } from 'react';
import CardArea from "../container/CardArea";
import SelectArea from "../container/SelectArea";

class PlayArea extends Component {
  render() {
    return (
      <div className="play-area">
        <CardArea/>
        <SelectArea/>
      </div>
    );
  }
}

export default PlayArea;
