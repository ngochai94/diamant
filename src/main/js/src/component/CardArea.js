import React, {Component} from 'react';
import Card from "./Card";

class CardArea extends Component {
  render() {
    const cards = this.props.cards.map((card, index) => (<Card key={`${card.name}${index}`} remain={card.remain} name={card.name}/>));
    return (
      <div className="card-area">
        {cards}
      </div>
    );
  }
}

export default CardArea;
