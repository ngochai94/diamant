import EarningArea from "./EarningArea";
import InactiveArea from "./InactiveArea";
import Login from "../container/Login";
import React, {Component} from 'react';
import Room from "../container/Room";
import User from "./User";
import Round from './Round';

class SideBar extends Component {
  render() {
    const users = this.props.users.sort((user1, user2) => user2.score - user1.score)
      .map(user => {
        return (<User key={user.name} {...user}/>);
      });
    const round = (<Round round={this.props.round}/>);
    const rooms = this.props.name ? (<Room/>) : (<Login/>);

    const display = Object.keys(this.props.users).length ? (<div>{users}{round}</div>) : rooms;

    return (
      <div className="side-bar">
        {display}
        <EarningArea earning={this.props.earning}/>
        <InactiveArea cards={this.props.inactiveCards}/>
      </div>
    );
  }
}

export default SideBar;