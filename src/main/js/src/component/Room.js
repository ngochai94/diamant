import React, {Component} from 'react';
import {List, Button, Icon} from "semantic-ui-react";
import User from './User';

class Room extends Component {
  render() {
    const currentRoom = this.props.currentRoom && this.props.currentRoom.participants && this.props.currentRoom.participants.map(user => {
      const status = user.ready ? 'done' : 'pending';
      return (<User key={user.name} name={user.name} status={status}/>)
    });

    const readyButton = (<Button onClick={this.props.onClickReady} fluid>Ready</Button>);

    const rooms = this.props.rooms.map(room => (
      <List.Item key={room.name}>
        <List.Content floated='right'>
          <Button onClick={() => this.props.onClickJoinRoom(room.name)}>Join</Button>
        </List.Content>
        <Icon circular inverted color='blue' name='game'/>
        <List.Content className='room-content'><List.Header>{room.participants[0].name}</List.Header></List.Content>
      </List.Item>
    ));

    const newRoomButton = (<Button onClick={this.props.onClickNewRoom} fluid>Create new room</Button>);

    const display = this.props.currentRoom ? (
      <div>
        {currentRoom}
        {readyButton}
      </div>
    ) : (
      <div>
        <List animated verticalAlign='middle'>
          {rooms}
        </List>
        {newRoomButton}
      </div>
    );

    return (
      <div className="room-area">
        {display}
      </div>
    );
  }
}

export default Room;
