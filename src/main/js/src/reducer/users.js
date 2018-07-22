const users = (state = [], action) => {
  switch (action.type) {
    case 'SERVER_ROOM':
      return action.users;
    default:
      return state
  }
};

export default users;
