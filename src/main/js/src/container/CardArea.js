import {connect} from 'react-redux';
import CardArea from "../component/CardArea";

const mapStateToProps = state => ({
  cards: state.cards
});

export default connect(
  mapStateToProps
)(CardArea);
