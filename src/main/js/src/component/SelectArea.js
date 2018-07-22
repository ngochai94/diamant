import React, {Component} from 'react';
import {Button} from 'semantic-ui-react'

class SelectArea extends Component {
  render() {
    const status = this.props.status;
    const disabled = status !== 'pending';
    const stopButton = status === 'stop' || status === 'pending' ? (
      <Button key='1' onClick={() => this.props.onClickChoice('stop')} disabled={disabled} size='huge' className='selected button-choice' color='red'>Stop</Button>) : null;
    const continueButton = status === 'continue' || status === 'pending' ? (
      <Button key='2' onClick={() => this.props.onClickChoice('continue')} disabled={disabled} size='huge' className='button-choice' color='blue'>Continue</Button>) : null;
    const inactive = status === 'inactive' ? (<div>Inactive</div>) : null;
    const buttons = [stopButton, continueButton, inactive];

    return (
      <div className='choice-area'>
        <Button.Group widths='2'>
          {buttons}
        </Button.Group>
      </div>
    );
  }
}

export default SelectArea;
