const rooms = (state = [], action) => {
  switch (action.type) {
    case 'SERVER_LOBBY':
      return action.rooms;
    default:
      return state;
  }
};

export default rooms;
