const currentUser = (state = {}, action) => {
  switch (action.type) {
    case 'SERVER_LOBBY':
      return action.currentRoom;
    default:
      return state
  }
};

export default currentUser;
