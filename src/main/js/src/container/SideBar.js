import {connect} from 'react-redux';
import SideBar from "../component/SideBar";

const mapStateToProps = state => ({
  users: state.users,
  earning: state.earning,
  inactiveCards: state.inactiveCards,
  name: state.name,
  round: state.round,
});

export default connect(
  mapStateToProps
)(SideBar);
