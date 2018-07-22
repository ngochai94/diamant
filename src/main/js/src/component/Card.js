import React, { Component } from 'react';
import CardImage from './CardImage';

class Card extends Component {
  render() {
    const gold = this.props.name.startsWith('gold') ? 'text-gold' : '';
    let remain, small = '';
    if (this.props.remain > 0) {
      remain = (<div className={`${gold} image-text`}>{this.props.remain}</div>);
    }
    if (this.props.small) {
      small = '-small';
    }
    return (
      <div className={`card${small}`}>
        <img src={CardImage[this.props.name]} alt=''/>
        {remain}
      </div>
    );
  }
}

export default Card;
