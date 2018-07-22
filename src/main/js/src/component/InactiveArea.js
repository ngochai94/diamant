import React, {Component} from 'react';
import Card from "./Card";
import {Icon} from "semantic-ui-react";

class InactiveArea extends Component {
  render() {
    const cards = this.props.cards.map(card => (<Card small key={card.name} name={card.name}/>));
    return (
      <div className="inactive-area">
        <Icon circular size='large' name='trash alternate'/>
        {cards}
      </div>
    );
  }
}

export default InactiveArea;
