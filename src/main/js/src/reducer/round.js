const round = (state = 0, action) => {
  switch (action.type) {
    case 'SERVER_ROOM':
      return action.round;
    default:
      return state
  }
};

export default round;
