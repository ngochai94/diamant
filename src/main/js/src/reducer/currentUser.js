const currentUser = (state = {}, action) => {
  switch (action.type) {
    case 'SERVER_ROOM':
      return action.currentUser;
    default:
      return state;
  }
};

export default currentUser;
