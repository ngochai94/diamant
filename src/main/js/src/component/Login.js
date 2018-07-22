import React, { Component } from 'react';
import {Button, Input} from "semantic-ui-react";

class Login extends Component {
  constructor(props) {
    super(props);
    this.state = {
      text: ''
    };
  }

  onSubmit = (name) => {
    if (name) this.props.onSubmit(name);
  };

  onKeyPress = (e) => {
    if (e.key === 'Enter' && e.target.value) {
      this.props.onSubmit(e.target.value);
    }
  };

  render() {
    return (
      <div className="login-area">
        <div className='login-input'>
        <Input onKeyPress={this.onKeyPress} onChange={(e) => this.setState({text: e.target.value.substr(0, 10)})} placeholder='Username'/>
        </div>
        <div className='login-join'>
        <Button onClick={() => this.onSubmit(this.state.text)} primary>Join</Button>
        </div>
      </div>
    );
  }
}

export default Login;
