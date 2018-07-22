import React, { Component } from 'react';
import {Icon} from "semantic-ui-react";

class EarningArea extends Component {
  render() {
    return (
      <div className="earning-area">
        <Icon circular size='large' name='money bill alternate outline'/>
        <div className='earning-text'>{this.props.earning}</div>
      </div>
    );
  }
}

export default EarningArea;
