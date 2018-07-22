import React, { Component } from 'react';
import pending from '../resource/pending.svg'
import done from '../resource/done.svg'
import inactive from '../resource/inactive.svg'

class User extends Component {
  render() {
    const icon = this.props.status === 'pending' ? pending : this.props.status === 'done' ? done : inactive;
    const rotating = this.props.status === 'pending' ? 'rotate' : '';

    return (
      <div className="user">
        <div className='user-name'>{this.props.name}</div>
        <div className='user-score'>{this.props.score}</div>
        <img className={`user-icon ${rotating}`} alt='' src={icon}/>
      </div>
    );
  }
}

export default User;
