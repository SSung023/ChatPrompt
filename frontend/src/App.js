import './App.css';
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

import Root from './pages/Root';
import EditDefinition from './pages/EditDefinition';
import EditInput from './pages/EditInput';
import Login from './pages/Login';
import PrivateRoute from './utility/routes/PrivateRoute';

const router = createBrowserRouter([
  {
    path: '/login',
    element: <Login/>
  },
  {
    path: '/',
    element: <PrivateRoute component={<Root />}/>,
    // element: <Root />,
    children: [
      { index: true, element: <EditDefinition /> } ,
      { path: '/input', element: <EditInput />},
    ]
  }
]);

function App() {
  return (
    <div>
      <RouterProvider router={router}/>
    </div>
  );
}

export default App;