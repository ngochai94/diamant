import {connect} from 'react-redux';
import SelectArea from "../component/SelectArea";
import {emit} from '../action/RoomSocket';

const mapStateToProps = state => ({
  status: state.currentUser.status
});

const mapDispatchToProps = dispatch => ({
  onClickChoice: choice => emit(choice)
});

export default connect(
  mapStateToProps,
  mapDispatchToProps
)(SelectArea);