import React, { Component } from 'react';

class Round extends Component {
  render() {
    return (
      <div className="round-area">
        {this.props.round}
      </div>
    );
  }
}

export default Round;
