import {combineReducers} from 'redux'
import users from "./users";
import currentUser from "./currentUser";
import earning from "./earning";
import cards from "./cards";
import inactiveCards from "./inactiveCards";
import name from "./name";
import rooms from "./rooms";
import currentRoom from "./currentRoom";
import round from "./round";

export default combineReducers({
  users,
  currentUser,
  earning,
  cards,
  inactiveCards,
  name,
  rooms,
  currentRoom,
  round,
});