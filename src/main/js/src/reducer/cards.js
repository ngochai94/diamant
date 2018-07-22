const cards = (state = [], action) => {
  switch (action.type) {
    case 'SERVER_ROOM':
      return action.cards;
    default:
      return state;
  }
};

export default cards;
