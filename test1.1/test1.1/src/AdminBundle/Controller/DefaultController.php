<?php

namespace AdminBundle\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\Controller;

class DefaultController extends Controller
{

    public function backAction()
    {

        return $this->render('@Admin/Default/back.html.twig');

    }
}