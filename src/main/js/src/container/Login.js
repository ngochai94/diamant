import {connect} from 'react-redux';
import Login from "../component/Login";
import {setName} from "../action";
import {init} from '../action/LobbySocket';

const mapDispatchToProps = dispatch => ({
  onSubmit: name => {
    init(dispatch, name);
    dispatch(setName(name));
  }
});

export default connect(
  null,
  mapDispatchToProps
)(Login);
