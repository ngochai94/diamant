import {connect} from 'react-redux';
import Room from "../component/Room";
import {choose} from "../action";
import {emit} from '../action/LobbySocket';

const mapStateToProps = state => ({
  rooms: state.rooms,
  currentRoom: state.currentRoom
});

const mapDispatchToProps = dispatch => ({
  onClickChoice: choice => dispatch(choose(choice)),
  onClickNewRoom: () => emit('new'),
  onClickJoinRoom: (room) => emit(`join|${room}`),
  onClickReady: () => emit('ready'),
  onClickLeave: () => emit('leave')
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(Room);
